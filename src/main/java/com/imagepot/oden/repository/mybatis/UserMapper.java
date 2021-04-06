package com.imagepot.oden.repository.mybatis;

import java.util.List;

import com.imagepot.oden.model.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 特定のユーザ情報を全て取得
    public User selectOneUser(Integer id);

    // 全ユーザの情報を取得
    public List<User> selectAllUser();

    // 特定のユーザを削除
    public boolean deleteOneUser(Integer id);

    // 特定のユーザのアイコンを変更
    public boolean updateIcon(User user);
}
