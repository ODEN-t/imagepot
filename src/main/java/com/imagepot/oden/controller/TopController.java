package com.imagepot.oden.controller;

import com.imagepot.oden.model.SigninForm;
import com.imagepot.oden.model.SignupForm;
import com.imagepot.oden.model.ValidationAll;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class TopController {
    @GetMapping
    public String getTopPage(@ModelAttribute SignupForm signupform, @ModelAttribute SigninForm signinform) {
        return "top";
    }

    @PostMapping(params = "signupAccount")
    public String postSignUp(
            @ModelAttribute @Validated(ValidationAll.class) SignupForm signupform,
            BindingResult resultSignup,
            SigninForm signinform,
            RedirectAttributes atts) {
        if (resultSignup.hasErrors()) {
            atts.addAttribute("hasErrors", true);
            return getTopPage(signupform, signinform);
        }
        return "redirect:/";
    }

    @PostMapping(params = "signinAccount")
    public String postSignIn(
            @ModelAttribute @Validated(ValidationAll.class) SigninForm signinform,
            BindingResult resultSignin,
            SignupForm signupform,
            RedirectAttributes atts) {
        if (resultSignin.hasErrors()) {
            atts.addAttribute("hasErrors", true);
            return getTopPage(signupform, signinform);
        }
        return "redirect:/";
    }

}
