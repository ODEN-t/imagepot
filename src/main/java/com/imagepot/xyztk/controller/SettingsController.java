//package com.imagepot.xyztk.controller;
//
//import com.imagepot.xyztk.model.AppUserDetails;
//import com.imagepot.xyztk.model.User;
//import com.imagepot.xyztk.repository.mybatis.UserMapper;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Controller
//@Slf4j
//public class SettingsController {
//
//    @Autowired
//    UserMapper userMapper;
//
//    @Autowired
//    @Qualifier("UserDetailsServiceImpl")
//    private UserDetailsService userDetailsService;
//
//    @GetMapping("/settings")
//    public String getSettings(Model model, @AuthenticationPrincipal AppUserDetails user) {
//        return "settings";
//    }
//
//    @PostMapping(value = "/settings/upload/newicon")
//    public String setNewIcon(
//            @RequestParam MultipartFile croppedImage, RedirectAttributes atts,
//            @AuthenticationPrincipal AppUserDetails userDetails) {
//        try {
//            String fileType = croppedImage.getContentType();
//            Long fileSize = croppedImage.getSize();
//            Long LIMIT_MB = 1L; // 1MB
//            Long LIMIT = LIMIT_MB * 1024 * 1024; // B to MB
//
//            // ファイル形式チェック
//            if (!(fileType.equals("image/jpeg") || fileType.equals("image/png"))) {
//                atts.addAttribute("hasErrors", true);
//                return "redirect:/settings";
//            }
//
//            // ファイルサイズチェック
//            if (fileSize > LIMIT) {
//                atts.addAttribute("hasErrors", true);
//                return "redirect:/settings";
//            }
//
//            Integer userId = userDetails.getUserId();
//            byte[] icon = croppedImage.getBytes();
//            String email = userDetails.getEmail();
//
//            User user = new User();
//            user.setId(userId);
//            user.setIcon(icon);
//            user.setEmail(email);
//
//            userMapper.updateIcon(user);
//            updateSecurityContext(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "redirect:/settings";
//    }
//
//    // DB更新後、ログインユーザのセキュリティ情報を更新
//    private void updateSecurityContext(User user) {
//        // ログイン中のユーザ情報を取得
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//
//        // ログインユーザのセキュリティ情報を取得
//        SecurityContext context = SecurityContextHolder.getContext();
//
//        // ログインユーザのセキュリティ情報を再設定
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
//                userDetails.getAuthorities()));
//
//        log.info("security context updated to {}", userDetails.getUsername());
//    }
//}
