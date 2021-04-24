package com.imagepot.xyztk.repository.mybatis;

import com.imagepot.xyztk.model.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignupFormMapper {

    // ユーザのemail重複チェック
    public boolean checkDuplicateEmail(String email);

    // Formからユーザ登録
    public Integer registUser(User user);
}
