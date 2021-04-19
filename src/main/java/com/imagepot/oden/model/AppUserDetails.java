package com.imagepot.oden.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDetails implements UserDetails {

    // Springで必要なフィールド
    private String userID; // ユーザID
    private String password; // パスワード
    private Date passUpdateDate; // パスワード更新日付
    private int loginMissTimes; // ログイン失敗回数
    private boolean unlock; // ロック状態フラグ
    private boolean enabled; // 有効・無効フラグ
    private Date userDueDate; // ユーザ有効期限
    private Collection<? extends GrantedAuthority> authority; // 権限

    // 独自フィールド
    private String email; // メールアドレス
    private String userName; // ユーザ名
    private byte[] icon; // アイコン

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    /**
     * アカウントの有効期限チェック
     * ture:有効 false:無効
     * 
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        if (this.userDueDate.after(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * アカウントのロックチェック
     * ture:有効 false:無効
     * 
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.unlock;
    }

    /**
     * パスワードの有効期限チェック
     * ture:有効 false:無効
     * 
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        if (this.passUpdateDate.after(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * アカウントの有効・無効チェック
     * ture:有効 false:無効
     * 
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
