package com.imgbucket.xyztk.util;

import com.imgbucket.xyztk.model.LoginUser;
import com.imgbucket.xyztk.service.LoginUserService;
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

    /**
     * 最適な単位にバイト変換する
     * @param size 変換対象の数字
     * @return 最適な単位を決定しバイト数と最適な単位を結合した文字列を返す
     */
    public String readableSize(double size) {
        String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int i = 0;
        while(size >= 1024) {
            size /= 1024;
            ++i;
        }
        return String.format("%." + 2 + "f " + units[i], size);
    }
}
