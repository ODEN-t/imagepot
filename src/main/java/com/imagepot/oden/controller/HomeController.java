package com.imagepot.oden.controller;

import java.util.List;

import com.imagepot.oden.model.User;
import com.imagepot.oden.service.StorageService;
import com.imagepot.oden.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    StorageService s3Service;

    @GetMapping("/home")
    public String getHome(Model model) {
        s3Service.getObjList();
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
}
