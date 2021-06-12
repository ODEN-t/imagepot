package com.imgbucket.xyztk.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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

    // 使用しないので常にtrue
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 使用しないので常にtrue
    @Override
    public boolean isAccountNonLocked() {
        return true;
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
                "user=" + user +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + authorities +
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
     * @return String base64変換後の文字列データ
     */
    static String encodeByteToBase64(byte[] image) {
        StringBuilder iconEncoded = new StringBuilder();
        String base64 = new String(Base64.getEncoder().encodeToString(image));
        iconEncoded.append("data:image/png;base64,");
        iconEncoded.append(base64);
        return iconEncoded.toString();
    }
}
