package com.imagepot.xyztk.controller;


import com.imagepot.xyztk.model.SignupForm;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.model.SignupFormAllValidations;
import com.imagepot.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserService userService, MessageSource messageSource, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * トップページを表示する
     * @return View
     */
    @GetMapping
    public String getTopPage(Model model) {
        SignupForm signupForm = new SignupForm();
        model.addAttribute(signupForm);
        return "top";
    }


    /**
     * ログイン失敗時エラーメッセージを表示する
     * @param status spring securityが付与するパラメータ
     * @return spring securityが付与するパラメータ
     */
    @GetMapping("login")
    public String getFailurePage(Model model, @RequestParam("auth") String status) {
        SignupForm signupForm = new SignupForm();
        model.addAttribute(signupForm);
        return status;
    }

    /**
     * ログインを試みる。成功時Home画面へ遷移
     * @return View
     */
    @PostMapping("login")
    public String postSignIn(Model model) {
        return "home";
    }

    /**
     * サインアップを試みる。バリデーションエラーがある場合はエラーメッセージとともにトップページを表示する。
     * 成功時はDBへユーザ登録し、サクセスメッセージとともにトップページを表示する。
     * @param signupForm ユーザが入力したフォーム情報
     * @param bindingResult バリデーション結果
     * @return View
     */
    @PostMapping ("signup")
    public String registerNewUser(
            @ModelAttribute @Validated({SignupFormAllValidations.class}) SignupForm signupForm,
            BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "top";
        }

        try {
            User user = new User();
            user.setName(signupForm.getName());
            user.setEmail(signupForm.getEmail());
            user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
            userService.addNewUser(user);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("error.signup",null, Locale.ENGLISH) + e.getMessage());
            model.addAttribute("registerError", true);
            model.addAttribute("message", e.getMessage());
            return "top";
        }

        model.addAttribute("registerSuccess", true);
        model.addAttribute("message", messageSource.getMessage("success.signup",null, Locale.ENGLISH));
        return "top";
    }
}
