package com.imagepot.xyztk.controller;


import com.imagepot.xyztk.model.SignupForm;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.model.ValidationAll;
import com.imagepot.xyztk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/top")
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

        User user = new User();
        user.setName(signupForm.getName());
        user.setEmail(signupForm.getEmail());
        user.setPassword(signupForm.getPassword());
        user.setRole("ROLE_USER");

        userService.addNewUser(user);
        return "home";
    }
}
