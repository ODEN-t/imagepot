package com.imgbucket.xyztk.controller;

import com.imgbucket.xyztk.model.SignupForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLogin() {
        return "login";
    }
//    @GetMapping("login")
//    public String getFailurePage(Model model, @RequestParam("auth") String status) {
//        SignupForm signupForm = new SignupForm();
//        model.addAttribute(signupForm);
//        return status;
//    }

    /**
     * ログインを試みる。成功時Home画面へ遷移
     * @return View
     */
    @PostMapping("login")
    public String postSignIn(Model model) {
        return "home";
    }
}
