package com.imgbucket.xyztk.controller;

import com.imgbucket.xyztk.model.LoginUser;
import com.imgbucket.xyztk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     *
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
     * 選択したユーザをDBから削除する。ajaxからコールされる。
     * @param userId ユーザID
     * @param loginUser ログインユーザ情報
     * @return リクエスト結果をjsに返却
     */
    @DeleteMapping("delete/{userId}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable long userId, @AuthenticationPrincipal LoginUser loginUser) {

        String result = "";
        if(userId == loginUser.id) {
            result = messageSource.getMessage("error.deleteUser", null, Locale.ENGLISH);
            return ResponseEntity.badRequest().body(result);
        }

        try {
            userService.deleteUser(userId);
            result = messageSource.getMessage("success.delete", null, Locale.ENGLISH);
        } catch (IllegalStateException e) {
            log.error(messageSource.getMessage("log.error", null, Locale.ENGLISH) + e.getMessage());
            result = messageSource.getMessage("error.critical", null, Locale.ENGLISH);
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}
