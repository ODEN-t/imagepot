package com.imagepot.oden.login.controller;

import com.imagepot.oden.login.model.Users;
import com.imagepot.oden.login.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    UsersService usersService;

    @GetMapping("/home")
    public String getHome(Model model) {
        Users users = usersService.selectOne("testid01");
        System.out.println("これがusers->" + users);
        return "home";
    }
}
