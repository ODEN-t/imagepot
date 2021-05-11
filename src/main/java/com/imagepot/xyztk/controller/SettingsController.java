package com.imagepot.xyztk.controller;

import com.google.common.base.Strings;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.UserInfo;
import com.imagepot.xyztk.model.UserInfoAllValidations;
import com.imagepot.xyztk.service.UserService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

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
    public String getSettings(Model model) {
        UserInfo userInfo = new UserInfo();
        Optional.ofNullable(model.getAttribute("hasErrors"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("errorMessage"))
                .ifPresent(model::addAttribute);

        model.addAttribute(userInfo);
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

            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                return "typeError";
            }

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

    @PostMapping("/edit/user/info")
    public String editUserNameAndEmail(
            @ModelAttribute @Validated({UserInfoAllValidations.class}) UserInfo userInfo,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "settings";
        }

        if(Strings.isNullOrEmpty(userInfo.getName()) && Strings.isNullOrEmpty(userInfo.getEmail())) {
            redirectAttributes.addFlashAttribute("hasErrors", true);
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("error.editUserNameAndEmail",null, Locale.ENGLISH));
            return "redirect:/settings";
        }

        try {
            int result = userService.updateUserInfo(loginUser.id, userInfo.getName(), userInfo.getEmail());
            switch (result) {
                case 1:
                    log.info(result + " column updated.");
                    break;
                case 2:
                    log.info(result + " column updated");
                    break;
            }
            model.addAttribute("message", "Updated Successfully!");
            utilComponent.updateSecurityContext(loginUser.email);

        } catch (Exception e) {
            model.addAttribute("message", "Error occurred.");
            e.printStackTrace();
        }
        return "redirect:/settings";
    }
}
