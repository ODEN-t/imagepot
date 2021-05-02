package com.imagepot.xyztk.controller;

import java.util.List;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.service.StorageService;
import com.imagepot.xyztk.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final StorageService s3Service;

    @Autowired
    public HomeController(UserService userService, StorageService s3Service) {
        this.userService = userService;
        this.s3Service = s3Service;
    }

    @GetMapping
    public String getHome() {
        s3Service.getObjList();
        return "home";
    }

    @GetMapping("/admin")
    public String getAdmin(Model model) {

        List<User> userList = userService.getUsers();
        model.addAttribute("userList", userList);

        int count = userList.size();
        model.addAttribute("count", count);
        return "admin";
    }

    @GetMapping("/settings")
    public String getSettings() { //Model model, @AuthenticationPrincipal AppUserDetails user
        return "settings";
    }

    @GetMapping("/logout")
    public String getLogout() {
        return "redirect:/";
    }
}
