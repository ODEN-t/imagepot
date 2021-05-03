package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

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
        Optional.ofNullable(model.getAttribute("deleteError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("deleteSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);
//        if(val.isPresent()) {
//            model.addAttribute(model.getAttribute("deleteError"));
//        }
//        if(val2.isPresent()) {
//            model.addAttribute(model.getAttribute("deleteSuccess"));
//        }
//        if(val3.isPresent()) {
//            model.addAttribute(model.getAttribute("message"));
//        }

        model.addAttribute("userList", userList);
        model.addAttribute("count", count);

        System.out.println(model);
        return "admin";
    }


    @DeleteMapping("delete/{userId}")
    public String deleteUser(@PathVariable long userId, RedirectAttributes redirectAttributes) {

        try {
            userService.deleteUser(userId);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("error.deleteUser",null, Locale.ENGLISH) + e.getMessage());
            redirectAttributes.addFlashAttribute("deleteError", true);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        System.out.println("success!!");
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.deleteUser",null, Locale.ENGLISH));
        return "redirect:/admin";
    }
}
