package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.UserService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final UtilComponent utilComponent;

    @Autowired
    public SettingsController(UserService userService, UtilComponent utilComponent, MessageSource messageSource) {
        this.userService = userService;
        this.utilComponent = utilComponent;
    }

    @GetMapping
    public String getSettings() {
        return "settings";
    }

    @PostMapping("/reset/icon")
    public String resetIcon(@AuthenticationPrincipal LoginUser loginUser) {
        long userId = loginUser.id;
        int num = userService.resetIcon(userId);
        log.info("Reset icon executed, UserID: " + loginUser.id + ". " + num + " column was updated.");
        utilComponent.updateSecurityContext(loginUser.email);
        return "redirect:/settings";
    }

    @PostMapping("/update/icon")
    @ResponseBody
    public String setNewIcon(
            @RequestParam MultipartFile croppedImage,
            @AuthenticationPrincipal LoginUser loginUser) {
        try {
            String fileType = croppedImage.getContentType();
            long fileSize = croppedImage.getSize();
            long LIMIT_MB = 1L; // 1MB
            long LIMIT = LIMIT_MB * 1024 * 1024; // B to MB

            // ファイル形式チェック
            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                return "typeError";
            }

            // ファイルサイズチェック
            if (fileSize > LIMIT) {
                return "sizeError";
            }

            long userId = loginUser.id;
            byte[] newIcon = croppedImage.getBytes();
            String email = loginUser.email;

            int num = userService.updateIcon(userId, newIcon);
            log.info("New icon was uploaded, UserID: " + loginUser.id + ". " + num + " column was updated.");
            utilComponent.updateSecurityContext(email);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
