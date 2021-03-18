package com.imagepot.oden.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopContorller {
    @GetMapping("/")
    public String getSignin(Model model) {
        return "top";
    }
}
