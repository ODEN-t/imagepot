package com.imagepot.oden.service;

import com.imagepot.oden.model.SignupForm;
import com.imagepot.oden.repository.mybatis.SignupFormMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignupFormService {
    @Autowired
    SignupFormMapper signupFormMapper;

    // ユーザのemail重複チェック
    public Integer checkDuplicateEmail(String email) {
        return signupFormMapper.checkDuplicateEmail(email);
    }

    // Formからユーザ登録
    public Integer registUser(SignupForm signupForm) {
        return signupFormMapper.registUser(signupForm);
    }
}
