package com.imagepot.xyztk.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String getTopPage() {
        return "top";
    }

    @PostMapping ("login")
    public String getLogin() {
        return "top";
    }
}
