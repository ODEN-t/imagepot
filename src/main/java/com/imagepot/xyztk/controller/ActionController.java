package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.FileService;
import com.imagepot.xyztk.service.StorageService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Slf4j
@Controller
@RequestMapping("/action")
public class ActionController {

    private final StorageService s3Service;
    private final FileService fileService;
    private final MessageSource messageSource;
    private final UtilComponent utilComponent;

    @Autowired
    public ActionController(StorageService s3Service, FileService fileService, MessageSource messageSource, UtilComponent utilComponent) {
        this.s3Service = s3Service;
        this.fileService = fileService;
        this.messageSource = messageSource;
        this.utilComponent = utilComponent;
    }

    @ModelAttribute
    List<PotFile> getFileList(@AuthenticationPrincipal LoginUser loginUser) {
        return fileService.getAllFilesById(loginUser);
    }

    @GetMapping
    public String getAction(
            List<PotFile> potFileList,
            Model model) {

        // get result message from deleteUser();
        Optional.ofNullable(model.getAttribute("actionError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("actionSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);

        double totalSize = 0;
        for (PotFile potFile : potFileList) {
            totalSize += potFile.getSize();
        }

        String totalSizeReadable = utilComponent.readableSize(totalSize);

        model.addAttribute("totalFiles", potFileList.size());
        model.addAttribute("totalSizeReadable", totalSizeReadable);
        model.addAttribute("fileList", potFileList);
        return "action";
    }

    @PostMapping(value = "/file", params = "delete")
    public String deleteImages(
            @RequestParam(value = "fileKey", required = false) String[] fileKeyList,
            RedirectAttributes redirectAttributes) {
        try {
            List<PotFile> deleteFileList = s3Service.getDeleteListAfterDeleteFiles(fileKeyList);
            fileService.deleteFilesByKey(deleteFileList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("actionError", true);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("error.deleteImage", null, Locale.ENGLISH));
            return "redirect:/action";
        }

        redirectAttributes.addFlashAttribute("actionSuccess", true);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.deleteImage", null, Locale.ENGLISH));
        return "redirect:/action";
    }

    @PostMapping(value = "/file", params = "download")
    public <T> T downloadImage(
            @AuthenticationPrincipal LoginUser loginUser,
            List<Image> imageList,
            HashMap<String, String> info,
            Model model,
            @RequestParam(value = "imgData", required = false) String[] checkedFileList) {

        if(checkedFileList == null) {
            model.addAttribute("actionError", true);
            model.addAttribute("message", messageSource.getMessage("error.downloadImage", null, Locale.ENGLISH));

            int totalImages = 0;
            double totalSize = 0;

            for (Image img : imageList) {
                totalSize += img.getRowSize();
                totalImages++;
            }

            String totalSizeReadable = utilComponent.readableSize(totalSize);

            model.addAttribute("totalImages", totalImages);
            model.addAttribute("totalSizeReadable", totalSizeReadable);
            return (T) "action";
        }

        ResponseEntity<byte[]> responseEntity = s3Service.downloadFile(checkedFileList, loginUser);
        model.addAttribute("images", imageList);
        return (T) responseEntity;
    }
}
