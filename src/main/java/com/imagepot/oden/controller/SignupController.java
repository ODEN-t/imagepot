package com.imagepot.oden.controller;

import com.imagepot.oden.model.SignupForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String getSignup(@ModelAttribute SignupForm form, Model model) {
        return "signup";
    }
}
