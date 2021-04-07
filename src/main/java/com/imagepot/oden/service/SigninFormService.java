package com.imagepot.oden.service;

import com.imagepot.oden.model.SigninForm;
import com.imagepot.oden.repository.mybatis.SigninFormMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SigninFormService {
    @Autowired
    SigninFormMapper signinFormMapper;

    // 入力されたemailに一致するアカウントが存在するかチェック
    public boolean checkEmailForSignin(String email) {
        return signinFormMapper.checkEmailForSignin(email);
    }

    // 入力されたemail,passwordに一致するアカウントが存在するかチェック
    public boolean auth(SigninForm signinForm) {
        return signinFormMapper.auth(signinForm);
    }
}
