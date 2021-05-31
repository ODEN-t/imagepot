package com.imagepot.xyztk.service;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public LoginUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * ログイン時に入力したメールアドレスと一致するユーザを返す
     * @param email ユーザが入力したメールアドレス
     * @return LoginUser ログインユーザ情報
     * @throws UsernameNotFoundException 入力したメールアドレスと一致するユーザ存在しない場合
     */
    @Override
    public LoginUser loadUserByUsername(String email) throws UsernameNotFoundException {
        assert(email != null);
        log.debug("loadUserByUsername(email):[{}]", email);
        LoginUser loginUser = userRepository.selectUserByEmail(email)
                .map(LoginUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by email:[" + email + "]"));
        return loginUser;
    }
}
