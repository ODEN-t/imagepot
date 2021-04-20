package com.imagepot.oden.controller;

import java.util.List;

import com.imagepot.oden.model.AppUserDetails;
import com.imagepot.oden.model.User;
import com.imagepot.oden.service.StorageService;
import com.imagepot.oden.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    StorageService s3Service;

    @GetMapping("/home")
    //@AuthenticationPrincipal AppUserDetails user
    public String getHome(Model model) {
        log.info("HomeController Start");
        //log.info(user.toString());
        // s3Service.getObjList();
        return "home";
    }

    @GetMapping("/admin")
    public String getAdmin(Model model) {

        List<User> userList = userService.selectAllUser();
        model.addAttribute("userList", userList);

        int count = userList.size();
        model.addAttribute("count", count);
        return "admin";
    }

    @PostMapping("/signout")
    public String getSignout() {
        System.out.println("Sign Out....");
        return "redirect:/";
    }

    @PostMapping("/home/icon")
    public void post(
            @RequestParam("upload_file") MultipartFile multipartFile,
            @RequestParam("filetype") String fileType,
            @AuthenticationPrincipal AppUserDetails user) {
        if (multipartFile.isEmpty()) {
            System.out.println("なんもないよ");
        }
        System.out.println(multipartFile);
        System.out.println(fileType);
    }
}
