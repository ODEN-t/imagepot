package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.PotFile;
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

import java.util.List;
import java.util.Locale;
import java.util.Optional;


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

    // ユーザの保持するファイルデータをDBから取得
    @ModelAttribute
    List<PotFile> getFileList(@AuthenticationPrincipal LoginUser loginUser) {
        return fileService.getAllFilesById(loginUser);
    }


    @GetMapping
    public String getAction(
            List<PotFile> potFileList,
            Model model) {

        // 実行結果メッセージ from deleteUser();
        Optional.ofNullable(model.getAttribute("actionError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("actionSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);

        String totalSizeReadable = utilComponent.readableSize(getTotalFileSize(potFileList));

        model.addAttribute("totalFiles", potFileList.size());
        model.addAttribute("totalSizeReadable", totalSizeReadable);
        model.addAttribute("fileList", potFileList);
        return "action";
    }

    // 選択されたファイルデータをs3とDBから削除
    @PostMapping(value = "/file", params = "delete")
    public String deleteImages(
            @RequestParam(value = "fileKey", required = false) String[] fileKeyList,
            RedirectAttributes redirectAttributes) {

        // s3とDBから削除
        try {
            List<PotFile> deleteFileList = s3Service.getDeleteListAfterDeleteFiles(fileKeyList);
            fileService.deleteFilesByKey(deleteFileList);
            redirectAttributes.addFlashAttribute("actionSuccess", true);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.deleteImage", null, Locale.ENGLISH));
        }
        // 未選択の場合エラーメッセージを返す
        catch (NullPointerException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("actionError", true);
            redirectAttributes.addFlashAttribute("message", messageSource.getMessage("error.deleteImage", null, Locale.ENGLISH));
        }

        return "redirect:/action";
    }

    // 選択されたファイルデータをs3からダウンロード
    @PostMapping(value = "/file", params = "download")
    public <T> T downloadImage(
            @AuthenticationPrincipal LoginUser loginUser,
            List<PotFile> potFileList,
            Model model,
            @RequestParam(value = "fileKey", required = false) String[] checkedFileList) {

        // s3からダウンロード
        try {
            ResponseEntity<byte[]> responseEntity = s3Service.downloadFile(checkedFileList, loginUser);
            return (T) responseEntity;
        }
        // 未選択の場合エラーメッセージを返す
        catch (NullPointerException e) {
            model.addAttribute("actionError", true);
            model.addAttribute("message", messageSource.getMessage("error.downloadImage", null, Locale.ENGLISH));
            return (T) "action";
        }
        // ファイルリスト、ファイル数、合計ファイルサイズを返す
        finally {
            String totalSizeReadable = utilComponent.readableSize(getTotalFileSize(potFileList));
            model.addAttribute("totalFiles", potFileList.size());
            model.addAttribute("totalSizeReadable", totalSizeReadable);
            model.addAttribute("fileList", potFileList);
        }
    }

    public double getTotalFileSize(List<PotFile> fileList) {
        double totalSize = 0;
        for (PotFile potFile : fileList) {
            totalSize += potFile.getSize();
        }
        return totalSize;
    }
}
