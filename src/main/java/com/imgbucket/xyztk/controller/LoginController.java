package com.imgbucket.xyztk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLogin() {
        return "login";
    }

    /**
     * ログインを試みる。成功時Home画面へ遷移
     * @return View
     */
    @PostMapping("login")
    public String postSignIn() {
        return "home";
    }
}
