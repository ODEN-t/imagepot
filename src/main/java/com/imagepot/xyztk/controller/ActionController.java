package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
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

import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/action")
public class ActionController {

    private final StorageService s3Service;
    private final MessageSource messageSource;
    private final UtilComponent utilComponent;

    @Autowired
    public ActionController(StorageService s3Service, MessageSource messageSource, UtilComponent utilComponent) {
        this.s3Service = s3Service;
        this.messageSource = messageSource;
        this.utilComponent = utilComponent;
    }

    @ModelAttribute
    List<Image> setImageList(@AuthenticationPrincipal LoginUser loginUser) {
        return s3Service.getObjList(loginUser);
    }

    @GetMapping
    public String getAction(
            @AuthenticationPrincipal LoginUser loginUser,
            List<Image> imageList,
            Model model) {

        // get result message from deleteUser();
        Optional.ofNullable(model.getAttribute("deleteError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("deleteSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);

        int totalImages = 0;
        double totalSize = 0;

        for (Image img : imageList) {
            totalSize += img.getRowSize();
            totalImages++;
        }

        String totalSizeReadable = utilComponent.readableSize(totalSize);

        model.addAttribute("totalImages", totalImages);
        model.addAttribute("totalSizeReadable", totalSizeReadable);
        model.addAttribute("imageList", imageList);
        return "action";
    }

    @PostMapping(value = "/file", params = "delete")
    public String deleteImages(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(value = "imgData", required = false) String[] imgData,
            RedirectAttributes redirectAttributes) {
        try {
            s3Service.deleteFile(imgData, loginUser);
        } catch (NullPointerException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("deleteError", true);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("error.deleteImage", null, Locale.ENGLISH));
            return "redirect:/action";
        }

        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.deleteImage", null, Locale.ENGLISH));
        return "redirect:/action";
    }

    @PostMapping(value = "/file", params = "download")
    public ResponseEntity<byte[]> downloadImage(
            @AuthenticationPrincipal LoginUser loginUser,
            List<Image> imageList,
            Model model,
            @RequestParam(value = "imgData", required = false) String[] checkedFileList) {

        ResponseEntity<byte[]> responseEntity = s3Service.downloadFile(checkedFileList, loginUser);
        model.addAttribute("images", imageList);
        return responseEntity;
    }
}
