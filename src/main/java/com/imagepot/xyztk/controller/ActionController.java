package com.imagepot.xyztk.controller;

import com.amazonaws.AmazonServiceException;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.service.FileService;
import com.imagepot.xyztk.service.StorageService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


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

    /**
     * ユーザの保持するファイル情報をDBから取得する
     * @param loginUser ログインユーザ情報
     * @return ログインユーザが保持するファイル情報
     */
    @ModelAttribute
    List<PotFile> getFileList(@AuthenticationPrincipal LoginUser loginUser) {
        return fileService.getAllFilesById(loginUser);
    }


    /**
     * 実行結果メッセージ、ファイル数、ファイル容量、ファイル情報をmodelにセットしaction.htmlでそれらを表示する
     * @param potFileList ユーザの保持する全ファイル情報を詰めたリスト
     * @param model 必要な情報をセットするモデル
     * @return View
     */
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

        List<String> readableSizeList = new ArrayList<>();
        for(PotFile p : potFileList)
            readableSizeList.add(utilComponent.readableSize(p.getSize()));

        model.addAttribute("totalFiles", potFileList.size());
        model.addAttribute("totalSizeReadable", totalSizeReadable);
        model.addAttribute("readableSizeList", readableSizeList);
        model.addAttribute("fileList", potFileList);
        return "action";
    }



    /**
     * ユーザが選択したファイルデータをs3とDBから削除しaction.htmlへリダイレクトさせる
     * @param fileKeyList ユーザが選択したファイルデータのキー
     * @return リダイレクト先のView
     */
    @PostMapping(value = "/file", params = "delete")
    public String deleteImages(
            @RequestParam(value = "fileKey", required = false) String[] fileKeyList,
            RedirectAttributes redirectAttributes) {

        // s3とDBから削除
        try {
            List<PotFile> deleteFileList = s3Service.s3DeleteFile(fileKeyList);
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


    /**
     * ユーザが選択したファイルデータをs3からダウンロード
     * @param loginUser ログインユーザ情報
     * @param potFileList ユーザの保持する全ファイル情報を詰めたリスト
     * @param checkedFileList ユーザが選択したファイルキーのリスト
     * @return View 成功時：ダウンロード実行  エラー時：エラーメッセージ表示
     */
    @PostMapping(value = "/file", params = "download")
    public <T> T downloadImage(
            @AuthenticationPrincipal LoginUser loginUser,
            List<PotFile> potFileList,
            Model model,
            @RequestParam(value = "fileKey", required = false) String[] checkedFileList) {

        // s3からダウンロード
        try {
            ResponseEntity<byte[]> responseEntity = s3Service.s3DownloadFile(checkedFileList, loginUser);
            return (T) responseEntity;
        }
        // 未選択の場合エラーメッセージを返す
        catch (IOException | NullPointerException | AmazonServiceException e) {
            model.addAttribute("actionError", true);
            model.addAttribute("message", messageSource.getMessage("error.downloadImage", null, Locale.ENGLISH));
            return (T) "action";
        }
        // ファイルリスト、ファイル数、合計ファイルサイズを返す
        finally {
            String totalSizeReadable = utilComponent.readableSize(getTotalFileSize(potFileList));
            List<String> readableSizeList = new ArrayList<>();
            for(PotFile p : potFileList)
                readableSizeList.add(utilComponent.readableSize(p.getSize()));
            model.addAttribute("readableSizeList", readableSizeList);
            model.addAttribute("totalFiles", potFileList.size());
            model.addAttribute("totalSizeReadable", totalSizeReadable);
            model.addAttribute("fileList", potFileList);
        }
    }

    /**
     * ユーザが選択したファイルをs3にアップロードし、成功時DBに更新分データを書き込む。リクエストはajax経由。
     * @param images ファイル情報を詰めたマルチパートファイルのリスト
     * @param loginUser ログインユーザ情報
     * @return ajaxへ実行結果を返す
     */
    @PostMapping("/upload")
    @ResponseBody
    @Async
    public CompletableFuture<String> uploadImage(@RequestParam ArrayList<MultipartFile> images, @AuthenticationPrincipal LoginUser loginUser) {
        List<PotFile> uploadedFiles = s3Service.s3UploadFile(images, loginUser);
        fileService.insertFiles(uploadedFiles);
        return CompletableFuture.completedFuture("success");
    }

    /**
     * ファイル情報を詰めたリストから合計ファイルサイズを計算する
     * @param fileList ファイル情報を詰めたリスト
     * @return 合計ファイルサイズ
     */
    public double getTotalFileSize(List<PotFile> fileList) {
        double totalSize = 0;
        for (PotFile potFile : fileList) {
            totalSize += potFile.getSize();
        }
        return totalSize;
    }

}
