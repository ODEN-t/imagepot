package com.imagepot.oden.repository.mybatis;

import com.imagepot.oden.model.SigninForm;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigninFormMapper {

    // 入力されたemailに一致するアカウントが存在するかチェック
    public boolean checkEmailForSignin(String email);

    // 入力されたemail,passwordに一致するアカウントが存在するかチェック
    public boolean auth(SigninForm signinForm);
}
