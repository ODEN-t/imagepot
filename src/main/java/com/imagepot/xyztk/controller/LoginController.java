package com.imagepot.xyztk.controller;


import com.imagepot.xyztk.model.SignupForm;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.model.ValidationAll;
import com.imagepot.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getTopPage(Model model) {
        SignupForm signupForm = new SignupForm();
        model.addAttribute(signupForm);
        return "top";
    }

    // auth with spring security
    @PostMapping("/login")
    public String postLogin() {
        return "home";
    }

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
            user.setPassword(signupForm.getPassword());
            userService.addNewUser(user);
        } catch (IllegalStateException e) {
            log.error("Throw Error when adding new user in LoginController. Message is: " + e.getMessage());
            model.addAttribute("registerError", true);
            model.addAttribute("message", e.getMessage());
            return "top";
        }

        model.addAttribute("registerSuccess", true);
        model.addAttribute("message", "Registration Successful! Please Login.");
        return "top";
    }
}
