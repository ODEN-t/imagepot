package com.imagepot.xyztk.controller;


import com.imagepot.xyztk.model.SignupForm;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.model.ValidationAll;
import com.imagepot.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping
    public String getTopPage(Model model) {
        SignupForm signupForm = new SignupForm();
        model.addAttribute(signupForm);
        return "top";
    }

    @PostMapping("login")
    public String postSignIn(Model model) {
        return "redirect:/home";
    }


    // auth with spring security
    // /loginでhomeが表示される。サーブレットエラーもでる
//    @PostMapping("/login")
//    public String login() {
//        return "home";
//    }

//    @GetMapping("/login")
//    public String login() {
//        return "home";
//    }

    // validation with entity RegisterForm
    @PostMapping ("/signup")
    public String registerNewUser(
            @ModelAttribute @Validated({ValidationAll.class}) SignupForm signupForm,
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
