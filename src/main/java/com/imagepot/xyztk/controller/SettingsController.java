package com.imagepot.xyztk.controller;

import com.google.common.base.Strings;
import com.imagepot.xyztk.model.*;
import com.imagepot.xyztk.service.UserService;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String getSettings(
            @ModelAttribute ChangeUserInfoForm changeUserInfoForm,
            @ModelAttribute ChangePasswordForm changePasswordForm,
            Model model) {


        // 実行結果のメッセージ取得
        Optional.ofNullable(model.getAttribute("hasErrors"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("errorMessage"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("successMessage"))
                .ifPresent(model::addAttribute);

        return "settings";
    }

    @PostMapping("/reset/user/icon")
    public String resetIcon(@AuthenticationPrincipal LoginUser loginUser) {
        long userId = loginUser.id;
        int num = userService.resetIcon(userId);
        log.info("Reset icon executed, UserID: " + loginUser.id + ". " + num + " column was updated.");
        utilComponent.updateSecurityContext(loginUser.email);
        return "redirect:/settings";
    }

    @PostMapping("/update/user/icon")
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

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return "success";
    }

    @PostMapping("/update/user/info")
    public String editUserNameAndEmail(
            @ModelAttribute ChangePasswordForm changePasswordForm,
            @ModelAttribute @Validated({ChangeUserInfoFormAllValidations.class}) ChangeUserInfoForm changeUserInfoForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {

        // バリデーションエラー
        if (result.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "settings";
        }

        User user = new User();
        user.setId(loginUser.id);
        user.setName(changeUserInfoForm.getName());
        user.setEmail(changeUserInfoForm.getEmail());

        boolean isNull = Strings.isNullOrEmpty(user.getName()) && Strings.isNullOrEmpty(user.getEmail());

        // どちらも未入力の場合
        if(isNull) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("error.editUserNameAndEmail",null, Locale.ENGLISH));
            return "redirect:/settings";
        }

        try {
            boolean isNewEmail = true;

            if(Strings.isNullOrEmpty(user.getName())) {
                 userService.updateEmail(user);
            } else if(Strings.isNullOrEmpty(user.getEmail())) {
                userService.updateName(user);
                isNewEmail = false;
            } else {
                userService.updateEmail(user);
                userService.updateName(user);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Updated Successfully!");

            if(isNewEmail)
                utilComponent.updateSecurityContext(user.getEmail());
            else
                utilComponent.updateSecurityContext(loginUser.email);

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/settings";
    }


    @PostMapping("/update/user/password")
    public String editUserPassword(
            @ModelAttribute @Validated({ChangePasswordFormAllValidations.class}) ChangePasswordForm changePasswordForm,
            BindingResult result,
            @ModelAttribute ChangeUserInfoForm changeUserInfoForm,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {

        // バリデーションエラー
        if (result.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "settings";
        }
        return "settings";
    }
}
