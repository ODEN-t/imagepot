package com.imagepot.xyztk.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginUser extends org.springframework.security.core.userdetails.User{

    private final User user;

    public LoginUser(User user) {
        super(user.getName(), user.getPassword(), user.isEnabled(), true,
                true, user.isUnlock(), convertGrantedAuthorities(user.getRole()));
        this.user = user;
    }

    public User getUser() {
        return user;
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
}
