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

import java.util.ArrayList;
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
    Image setImageList(@AuthenticationPrincipal LoginUser loginUser) {
        return s3Service.getObjList(loginUser);
    }

    @GetMapping
    public String getHome(
            @AuthenticationPrincipal LoginUser loginUser,
            Image imageList,
            Model model) {
        log.info(loginUser.toString());
        model.addAttribute("imageList", imageList);
        return "home";
    }

    @PostMapping("/upload")
    @ResponseBody
    @Async
    public CompletableFuture<String> uploadImage(@RequestParam ArrayList<MultipartFile> images, @AuthenticationPrincipal LoginUser loginUser) {
        for (MultipartFile image : images) {
            s3Service.uploadFile(image, loginUser);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("success");
    }

}
