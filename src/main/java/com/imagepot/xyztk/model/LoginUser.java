package com.imagepot.xyztk.model;

import com.google.common.base.Strings;
import com.google.common.primitives.Bytes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginUser implements UserDetails {

    // sec:authentication の場合User経由でフィールドアクセス
    private final User user;
    // ${#authentication.principal} の場合フィールド経由でアクセス
    public final long id;
    public final String name;
    public final String email;
    public final String password;
    public final String icon;
    public final Set<GrantedAuthority> authorities;
    public final Integer loginMissTimes;
    public final boolean unlocked;
    public final boolean enabled;

    public LoginUser(User user) {
        this.user = user;
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        if(Optional.ofNullable(user.getIcon()).isPresent()) {
            this.icon = encodeByteToBase64(user.getIcon());
        } else {
            this.icon = null;
        }
        this.authorities = convertGrantedAuthorities(user.getRole());
        this.loginMissTimes = user.getLoginMissTimes();
        this.unlocked = user.isUnlock();
        this.enabled = user.isEnabled();
    }


    public User getUser() {
        return user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.unlocked;
    }

    // 使用しないので常にtrue
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 使用しないので常にtrue
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
//                ", icon=" + icon +
                ", authorities=" + authorities +
                ", loginMissTimes=" + loginMissTimes +
                ", unlocked=" + unlocked +
                ", enabled=" + enabled +
                '}';
    }

    /**
     * カンマ区切りのロールをSimpleGrantedAuthorityのコレクションへ変換する
     *
     * @param roles カンマ区切りのロール
     * @return SimpleGrantedAuthorityのコレクション
     */
    static Set<GrantedAuthority> convertGrantedAuthorities(String roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }
        Set<GrantedAuthority> authorities = Stream.of(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return authorities;
    }

    /**
     * 画像のbyte配列をbase64形式に変換する
     * @param image 画像のbyte配列
     * @return Optional<String> base64変換後のString
     */
    static String encodeByteToBase64(byte[] image) {
        StringBuilder iconEncoded = new StringBuilder();
        String base64 = new String(Base64.getEncoder().encodeToString(image));
        iconEncoded.append("data:image/png;base64,");
        iconEncoded.append(base64);
        return iconEncoded.toString();
    }
}
