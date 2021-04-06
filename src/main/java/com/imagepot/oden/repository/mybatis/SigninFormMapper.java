package com.imagepot.oden.repository.mybatis;

import com.imagepot.oden.model.SigninForm;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigninFormMapper {

    // Formに入力されたemailに一致するアカウントが存在するかチェック
    public Integer checkEmailForSignin(String email);

    // Formに入力されたemial・passwordが一致するアカウントが存在するかチェック
    // emailの場合とpasswordの場合で異なるメッセージを出力したいので分離 できれば一つにしたい
    public Integer checkEmailAndPasswordForSignin(SigninForm signinForm);
}
