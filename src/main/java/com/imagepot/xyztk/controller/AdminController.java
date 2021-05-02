package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final MessageSource messageSource;

    public AdminController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getAdmin(Model model) {
        List<User> userList = userService.getUsers();
        int count = userList.size();
        model.addAttribute("userList", userList);
        model.addAttribute("count", count);
        return "admin";
    }

    @DeleteMapping("delete/{userId}")
    public String deleteUser(@PathVariable long userId, Model model) {

        try {
            userService.deleteUser(userId);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("error.deleteUser",null, Locale.ENGLISH) + e.getMessage());
            model.addAttribute("registerError", true);
            model.addAttribute("message", e.getMessage());
        }

        model.addAttribute("registerSuccess", true);
        model.addAttribute("message", messageSource.getMessage("success.deleteUser",null, Locale.ENGLISH));
        return "redirect:/admin";
    }
}
