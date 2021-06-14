package com.imgbucket.xyztk.controller;


import com.imgbucket.xyztk.model.SignupForm;
import com.imgbucket.xyztk.model.User;
import com.imgbucket.xyztk.model.SignupFormAllValidations;
import com.imgbucket.xyztk.service.UserService;
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
public class TopController {

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
}
