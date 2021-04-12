package com.imagepot.oden.repository.mybatis;

import java.util.List;

import com.imagepot.oden.model.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 特定のユーザの情報を全て取得
    public User selectOneUser(Integer id);

    // 全ユーザを取得
    public List<User> selectAllUser();

    // 特定のユーザを削除
    public boolean deleteOneUser(Integer id);

    // 特定のユーザアイコンを更新
    public boolean updateIcon(User user);

    // 特定のユーザログイン状況を更新
    public boolean updateLoggingIn(User user);
}
