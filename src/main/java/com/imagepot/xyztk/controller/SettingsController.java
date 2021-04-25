package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.AppUserDetails;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.repository.mybatis.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/settings")
    public String getSettings(Model model, @AuthenticationPrincipal AppUserDetails user) {
        return "settings";
    }

    @PostMapping(value = "/settings/upload/newicon")
    public String setNewIcon(@AuthenticationPrincipal AppUserDetails auth,
            @RequestParam MultipartFile croppedImage, RedirectAttributes atts) {
        try {
            String fileType = croppedImage.getContentType();
            Long fileSize = croppedImage.getSize();
            Long LIMIT_MB = 1L; // 1MB
            Long LIMIT = LIMIT_MB * 1024 * 1024; // B to MB

            // ファイル形式チェック
            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
                atts.addAttribute("hasErrors", true);
                return "redirect:/settings";
            }

            // ファイルサイズチェック
            if (fileSize > LIMIT) {
                atts.addAttribute("hasErrors", true);
                return "redirect:/settings";
            }

            Integer userId = 1;
            System.out.println("userid => " + userId);
            byte[] icon = croppedImage.getBytes();

            User user = new User();
            user.setId(userId);
            user.setIcon(icon);

            Boolean result = userMapper.updateIcon(user);
            if (result) {
                System.out.println("Update成功");
            } else {
                System.out.println("Update失敗");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/settings";
    }
}
