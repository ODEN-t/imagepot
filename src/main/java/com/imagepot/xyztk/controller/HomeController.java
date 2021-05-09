package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    private final StorageService s3Service;

    @Autowired
    public HomeController(StorageService s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping
    public String getHome(@AuthenticationPrincipal LoginUser loginUser) {
        s3Service.getObjList();
        log.info(loginUser.toString());
        return "home";
    }

    @GetMapping("/settings")
    public String getSettings(@AuthenticationPrincipal LoginUser loginUser) {
        return "settings";
    }

}
