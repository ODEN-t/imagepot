package com.imgbucket.xyztk.controller;

import com.google.common.base.Strings;
import com.imgbucket.xyztk.model.*;
import com.imgbucket.xyztk.service.UserService;
import com.imgbucket.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController {

    private final UserService userService;
    private final UtilComponent utilComponent;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SettingsController(UserService userService, UtilComponent utilComponent, MessageSource messageSource, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.utilComponent = utilComponent;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 設定画面を表示する
     * @param updateUserInfoForm ユーザ情報(ユーザ名。メールアドレス)を変更するフォーム
     * @param updateUserPasswordForm ユーザパスワードを変更するフォーム
     * @return View
     */
    @GetMapping
    public String getSettings(
            @ModelAttribute UpdateUserInfoForm updateUserInfoForm,
            @ModelAttribute UpdateUserPasswordForm updateUserPasswordForm,
            Model model) {

        Optional.ofNullable(model.getAttribute("hasErrors"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("errorMessage"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("successMessage"))
                .ifPresent(model::addAttribute);

        return "settings";
    }


    /**
     * ユーザアイコンをデフォルトに戻す
     * @param loginUser ログインユーザ情報
     * @return リダイレクト後のView
     */
    @PostMapping("/reset/user/icon")
    public String resetIcon(@AuthenticationPrincipal LoginUser loginUser) {
        userService.resetIcon(loginUser);
        utilComponent.updateSecurityContext(loginUser.email);
        return "redirect:/settings";
    }


    /**
     * ユーザのアイコンを変更を変更する。ajax経由でリクエストを受け取りDBへ変更後の画像データを登録する。
     * @param croppedImage ユーザがアップロードした画像をクロップしたデータ
     * @param loginUser ログインユーザ情報
     * @return 成功時：ajaxへ返す 失敗時：設定画面へリダイレクト
     */
    @PostMapping("/update/user/icon")
    public ResponseEntity<String> updateUserIcon(
            @RequestParam MultipartFile croppedImage,
            @AuthenticationPrincipal LoginUser loginUser) {
        try {
            String fileType = croppedImage.getContentType();
            long MAX_FILE_SIZE_MB = 1L; // 1MB
            long MAX_FILE_SIZE = MAX_FILE_SIZE_MB * 1024 * 1024; // B to MB

            if (!(Objects.requireNonNull(fileType).equals("image/jpeg") || fileType.equals("image/png")))
                return new ResponseEntity<>(messageSource.getMessage("error.updateUserIcon.type", null, Locale.ENGLISH), new HttpHeaders(), HttpStatus.BAD_REQUEST);

            if (croppedImage.getSize() > MAX_FILE_SIZE)
                return new ResponseEntity<>(messageSource.getMessage("error.updateUserIcon.size", null, Locale.ENGLISH), new HttpHeaders(), HttpStatus.BAD_REQUEST);

            userService.updateIcon(loginUser, croppedImage.getBytes());
            utilComponent.updateSecurityContext(loginUser.email);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return new ResponseEntity<>(messageSource.getMessage("error.critical", null, Locale.ENGLISH), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(messageSource.getMessage("success.update", null, Locale.ENGLISH), new HttpHeaders(), HttpStatus.OK);
    }


    /**
     * ユーザが任意にユーザ名、メールアドレスを更新する。
     * @param updateUserPasswordForm パスワード更新フォーム
     * @param updateUserInfoForm ユーザ名、メールアドレス更新フォーム
     * @param result バリデーション結果
     * @param loginUser ログインユーザ情報
     * @return View
     */
    @PostMapping("/update/user/info")
    public String updateUserInfo(
            @ModelAttribute UpdateUserPasswordForm updateUserPasswordForm,
            @ModelAttribute @Validated({UpdateUserInfoFormAllValidations.class}) UpdateUserInfoForm updateUserInfoForm,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {

        // バリデーションエラー(redirectの場合エラーメッセージ表示できない)
        if (result.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "settings";
        }

        User user = new User();
        user.setId(loginUser.id);
        user.setName(updateUserInfoForm.getName());
        user.setEmail(updateUserInfoForm.getEmail());

        boolean isNull = Strings.isNullOrEmpty(user.getName()) && Strings.isNullOrEmpty(user.getEmail());

        // どちらも未入力の場合
        if(isNull) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("error.updateUserInfo",null, Locale.ENGLISH));
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
            redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("success.update",null, Locale.ENGLISH));

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


    /**
     * ユーザが任意にパスワード更新する。
     * @param updateUserPasswordForm パスワード更新フォーム
     * @param result バリデーション結果
     * @param updateUserInfoForm ユーザ名、メールアドレス更新フォーム
     * @param loginUser ログインユーザ情報
     * @return View
     */
    @PostMapping("/update/user/password")
    public String updateUserPassword(
            @ModelAttribute @Validated({UpdateUserPasswordFormAllValidations.class}) UpdateUserPasswordForm updateUserPasswordForm,
            BindingResult result,
            @ModelAttribute UpdateUserInfoForm updateUserInfoForm,
            Model model,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal LoginUser loginUser) {

        // バリデーションエラー(redirectの場合エラーメッセージ表示できない)
        if (result.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "settings";
        }


        // 現パスワードが間違っている or 現新パスワードが一致する場合はエラー
        boolean isPasswordMatched = passwordEncoder.matches(updateUserPasswordForm.getCurrentPassword(), loginUser.getPassword());
        boolean isSame = passwordEncoder.matches(updateUserPasswordForm.getNewPassword(), loginUser.getPassword());

        if(!(isPasswordMatched)) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("error.updateUserPassword.notMatch",null, Locale.ENGLISH));
            return "redirect:/settings";
        }

        if(isSame) {
            redirectAttributes.addFlashAttribute("errorMessage", messageSource.getMessage("error.updateUserPassword.isSame",null, Locale.ENGLISH));
            return "redirect:/settings";
        }

        User user = new User();
        user.setId(loginUser.id);
        userService.updatePassword(user, passwordEncoder.encode(updateUserPasswordForm.getNewPassword()));

        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("success.update",null, Locale.ENGLISH));

        utilComponent.updateSecurityContext(loginUser.email);

        return "redirect:/settings";
    }
}
