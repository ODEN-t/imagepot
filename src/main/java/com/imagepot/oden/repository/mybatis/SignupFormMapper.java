package com.imagepot.oden.repository.mybatis;

import com.imagepot.oden.model.SignupForm;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignupFormMapper {

    // ユーザのemail重複チェック
    public Integer checkDuplicateEmail(String email);

    // Formからユーザ登録
    public Integer registUser(SignupForm signupForm);
}
