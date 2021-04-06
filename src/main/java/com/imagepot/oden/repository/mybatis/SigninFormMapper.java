package com.imagepot.oden.repository.mybatis;

import com.imagepot.oden.model.SigninForm;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigninFormMapper {

    // Formに入力されたユーザが存在するかチェック
    public Integer checkUserForSignin(SigninForm signinForm);
}
