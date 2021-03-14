package com.imagepot.oden.controller;

import com.imagepot.oden.model.SignupForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @GetMapping("/signup")
    public String getSignup(@ModelAttribute SignupForm form, Model model) {
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignUp(@ModelAttribute SignupForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/signup";
        }

        System.out.println(form);

        return "redirect:/";
    }
}