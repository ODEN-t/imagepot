package com.imagepot.oden.controller;

import com.imagepot.oden.model.SignupForm;
import com.imagepot.oden.model.ValidationAll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TopContorller {
    @GetMapping("/")
    public String getSignin(@ModelAttribute SignupForm form, Model model) {
        return "top";
    }

    @PostMapping("/")
    public String postSignUp(@ModelAttribute @Validated(ValidationAll.class) SignupForm form, BindingResult result,
            Model model, RedirectAttributes atts) {
        if (result.hasErrors()) {
            System.out.println(form);
            atts.addAttribute("hasErrors", true);
            return "top";
        }

        return "redirect:/";
    }
}
