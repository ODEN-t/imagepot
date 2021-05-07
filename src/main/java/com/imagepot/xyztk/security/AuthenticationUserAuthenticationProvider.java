package com.imagepot.xyztk.security;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class AuthenticationUserAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthenticationUserAuthenticationProvider(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        final String email = (String) auth.getPrincipal();
        final String password = (String) auth.getCredentials();

        final Optional<User> user = userService.getUserByEmail(email);
        Collection<GrantedAuthority> authorityList = new ArrayList<>();

        final Optional<Authentication> result = user.map(u -> {
            if(u.getRole().equals("ROLE_ADMIN")) {
                authorityList.add(new SimpleGrantedAuthority(ApplicationUserRole.ADMIN.name()));
            } else if(u.getRole().equals("ROLE_USER")) {
                authorityList.add(new SimpleGrantedAuthority(ApplicationUserRole.USER.name()));
            } else {
                throw new BadCredentialsException("Authority Error");
            }
            if (passwordEncoder.matches(password, u.getPassword())) {
                return new UsernamePasswordAuthenticationToken(email, password, authorityList);
            }
            return new UsernamePasswordAuthenticationToken(email, password, authorityList);
        });
        return result.orElseThrow(() -> new BadCredentialsException("illegal email or password"));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
