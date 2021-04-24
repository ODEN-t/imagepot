package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.SignupForm;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.model.ValidationAll;
import com.imagepot.xyztk.service.SignupFormService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TopController {

    @Autowired
    SignupFormService signupFormService;

    @GetMapping("/")
    public String getTopPage(@ModelAttribute SignupForm signupform) {
        System.out.println("getTopPage走る");
        return "top";
    }

    // @GetMapping("/login")
    // public String getLogin(@ModelAttribute SignupForm signupform) {
    // System.out.println("getLogin走る");
    // return "top";
    // }

    @PostMapping(value = "signup", params = "signupAccount")
    public String postSignUp(
            @ModelAttribute @Validated(ValidationAll.class) SignupForm signupform,
            BindingResult resultSignup,
            RedirectAttributes atts,
            Model model) {
        if (resultSignup.hasErrors()) {
            atts.addAttribute("hasErrors", true);
            return getTopPage(signupform);
        }

        User user = new User();
        user.setEmail(signupform.getSignupEmail());
        user.setPassword(signupform.getSignupPassword());
        user.setName(signupform.getName());
        Integer result = signupFormService.registUser(user);
        if (result == 1) {
            System.out.println("insert成功");
            model.addAttribute("registSuccess", true);
        } else {
            System.out.println("insert失敗");
        }

        return "redirect:/";
    }

    @PostMapping(params = "signinAccount")
    public String postSignIn(Model model) {
        return "redirect:/home";
    }

    @ExceptionHandler(DataAccessException.class)
    public String dataAccessExceptionHandler(DataAccessException e, Model model) {
        model.addAttribute("error", "内部サーバエラー(DB)：ExceptionHandler");
        model.addAttribute("message", "SignupControllerでDataAccessExceptionが発生しました。");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model) {
        model.addAttribute("error", "内部サーバエラー：ExceptionHandler");
        model.addAttribute("message", "SignupControllerでExceptionが発生しました。");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);
        return "error";
    }
}
