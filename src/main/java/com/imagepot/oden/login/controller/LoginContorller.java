package com.imagepot.oden.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginContorller {
    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }
}
