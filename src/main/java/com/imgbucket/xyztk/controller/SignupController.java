package com.imgbucket.xyztk.controller;

import com.imgbucket.xyztk.model.SignupForm;
import com.imgbucket.xyztk.model.SignupFormAllValidations;
import com.imgbucket.xyztk.model.User;
import com.imgbucket.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignupController(UserService userService, MessageSource messageSource, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getSignupPage(Model model) {
        SignupForm signupForm = new SignupForm();
        model.addAttribute(signupForm);
        return "signup";
    }

    /**
     * サインアップを試みる。バリデーションエラーがある場合はエラーメッセージとともにトップページを表示する。
     * 成功時はDBへユーザ登録し、サクセスメッセージとともにログインページを表示する。
     * @param signupForm ユーザが入力したフォーム情報
     * @param bindingResult バリデーション結果
     * @return View
     */
    @PostMapping
    public String registerNewUser(
            @ModelAttribute @Validated({SignupFormAllValidations.class}) SignupForm signupForm,
            BindingResult bindingResult,
            Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "signup";
        }

        try {
            User user = new User();
            user.setName(signupForm.getName());
            user.setEmail(signupForm.getEmail());
            user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
            userService.insertNewUser(user);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("log.error",null, Locale.ENGLISH) + e.getMessage());
            model.addAttribute("registerError", true);
            model.addAttribute("message", e.getMessage());
            return "signup";
        }

        model.addAttribute("registerSuccess", true);
        model.addAttribute("message", messageSource.getMessage("success.signup",null, Locale.ENGLISH));
        return "login";
    }
}
