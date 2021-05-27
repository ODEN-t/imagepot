package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.FileService;
import com.imagepot.xyztk.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    private final StorageService s3Service;
    private final FileService fileService;

    @Autowired
    public HomeController(StorageService s3Service, FileService fileService) {
        this.s3Service = s3Service;
        this.fileService = fileService;
    }

    @ModelAttribute
    List<PotFile> getFileList(@AuthenticationPrincipal LoginUser loginUser) {
        return fileService.getAllFilesById(loginUser);
    }

    @GetMapping
    public String getHome(
            @AuthenticationPrincipal LoginUser loginUser,
            List<PotFile> potFileList,
            Model model) {
        log.info(loginUser.toString());
        List<URL> urlList = new ArrayList<>();
        for(PotFile potFile : potFileList) {
            urlList.add(potFile.getUrl());
        }
        model.addAttribute("urlList", urlList);
        return "home";
    }

    @PostMapping("/upload")
    @ResponseBody
    @Async
    public CompletableFuture<String> uploadImage(@RequestParam ArrayList<MultipartFile> images, @AuthenticationPrincipal LoginUser loginUser) {
        List<PotFile> uploadedFiles = s3Service.getUploadedFilesAfterUpload(images, loginUser);
        fileService.insertFiles(uploadedFiles);
        return CompletableFuture.completedFuture("success");
    }
}
