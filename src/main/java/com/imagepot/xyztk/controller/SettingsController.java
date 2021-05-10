package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.UserService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final UtilComponent utilComponent;
    private final MessageSource messageSource;

    @Autowired
    public SettingsController(UserService userService, UtilComponent utilComponent, MessageSource messageSource) {
        this.userService = userService;
        this.utilComponent = utilComponent;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String getSettings(@AuthenticationPrincipal LoginUser loginUser,
                              Model model) {
        // get result message from deleteUser();
//        Optional.ofNullable(model.getAttribute("updateIconError"))
//                .ifPresent(model::addAttribute);
//        Optional.ofNullable(model.getAttribute("updateIconSuccess"))
//                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute(""))
                .ifPresent(model::addAttribute);



        System.out.println(model);
        return "settings";
    }

    @PostMapping(value = "/upload/newicon")
    public String setNewIcon(
            @RequestParam MultipartFile croppedImage,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {
        try {
            String fileType = croppedImage.getContentType();
            long fileSize = croppedImage.getSize();
            long LIMIT_MB = 1L; // 1MB
            long LIMIT = LIMIT_MB * 1024 * 1024; // B to MB

            // ファイル形式チェック
            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                redirectAttributes.addFlashAttribute("updateIconError", true);
                redirectAttributes.addFlashAttribute("message", messageSource.getMessage("error.updateIcon", null, Locale.ENGLISH));
                return "redirect:/settings";
            }

            // ファイルサイズチェック
            if (fileSize > LIMIT) {
                redirectAttributes.addFlashAttribute("updateIconError", true);
                redirectAttributes.addFlashAttribute("message", messageSource.getMessage("error.updateIcon", null, Locale.ENGLISH));
                return "redirect:/settings";
            }

            long userId = loginUser.id;
            byte[] newIcon = croppedImage.getBytes();
            String email = loginUser.email;

            userService.updateIcon(userId, newIcon);
            utilComponent.updateSecurityContext(email);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //redirectAttributes.getFlashAttributes().clear();
        redirectAttributes.addFlashAttribute("updateIconSuccess", true);
        redirectAttributes.addFlashAttribute("", "");

        return "redirect:/settings";
    }
}
