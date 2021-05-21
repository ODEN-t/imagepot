package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
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

    @Autowired
    public HomeController(StorageService s3Service) {
        this.s3Service = s3Service;
    }

    @ModelAttribute
    List<Image> setImageList(@AuthenticationPrincipal LoginUser loginUser) {
        return s3Service.getObjList(loginUser);
    }

    @GetMapping
    public String getHome(
            @AuthenticationPrincipal LoginUser loginUser,
            List<Image> imageList,
            Model model) {
        log.info(loginUser.toString());
        List<URL> urlList = new ArrayList<>();
        for(Image image : imageList) {
            urlList.add(image.getUrl());
        }
        model.addAttribute("urlList", urlList);
        return "home";
    }

    @PostMapping("/upload")
    @ResponseBody
    @Async
    public CompletableFuture<String> uploadImage(@RequestParam ArrayList<MultipartFile> images, @AuthenticationPrincipal LoginUser loginUser) {
        s3Service.uploadFile(images, loginUser);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("success");
    }

}
