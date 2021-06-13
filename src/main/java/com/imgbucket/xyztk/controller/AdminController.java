package com.imgbucket.xyztk.controller;

import com.imgbucket.xyztk.model.User;
import com.imgbucket.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
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

    /**
     * adminのViewとともにユーザ数、実行結果メッセージを表示する。
     * @param model 必要な情報をセットするモデル
     * @return View
     */
    @GetMapping
    public String getAdmin(Model model) {

        model.addAttribute("userList", userService.getUsers());
        model.addAttribute("count", userService.getUsers().size());

        Optional.ofNullable(model.getAttribute("deleteError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("deleteSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);

        return "admin";
    }


    /**
     * ユーザ情報をDBから削除する
     * @param userId ユーザID
     * @param redirectAttributes リダイレクト先へパラメータを渡す
     * @return リダイレクト先View
     */
    @DeleteMapping("delete/{userId}")
    public String deleteUser(@PathVariable long userId, RedirectAttributes redirectAttributes) {

        try {
            userService.deleteUser(userId);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("log.error",null, Locale.ENGLISH) + e.getMessage());
            redirectAttributes.addFlashAttribute("deleteError", true);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.delete",null, Locale.ENGLISH));

        return "redirect:/admin";
    }
}
