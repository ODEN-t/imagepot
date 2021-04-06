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

    public Integer checkEmailForSignin(String email) {
        return signinFormMapper.checkEmailForSignin(email);
    }

    public Integer checkEmailAndPasswordForSignin(SigninForm signinForm) {
        return signinFormMapper.checkEmailAndPasswordForSignin(signinForm);
    }
}
