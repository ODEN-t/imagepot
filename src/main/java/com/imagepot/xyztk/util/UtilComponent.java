package com.imagepot.xyztk.util;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.LoginUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UtilComponent {

    private final LoginUserService loginUserService;

    public UtilComponent(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }


    /**
     * DB更新後セキュリティコンテキストを更新する
     * @param email ユーザのメールアドレス
     */
    public void updateSecurityContext(String email) {
        LoginUser loginUser = loginUserService.loadUserByUsername(email);
        SecurityContext context = SecurityContextHolder.getContext();

        // ログインユーザのセキュリティ情報を再設定
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, loginUser.getPassword(),
                loginUser.getAuthorities()));

        log.info("Security context updated to {}", loginUser.getUsername());
    }
}