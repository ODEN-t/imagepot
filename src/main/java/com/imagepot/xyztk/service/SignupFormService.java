package com.imagepot.xyztk.service;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.repository.mybatis.SignupFormMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupFormService {

    @Autowired
    SignupFormMapper signupFormMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    // ユーザのemail重複チェック
    public boolean checkDuplicateEmail(String email) {
        return signupFormMapper.checkDuplicateEmail(email);
    }

    // Formからユーザ登録
    public Integer registUser(User user) {

        // パスワードをハッシュ化
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        return signupFormMapper.registUser(user);
    }
}
