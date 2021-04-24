package com.imagepot.xyztk.repository.mybatis;

import java.util.List;

import com.imagepot.xyztk.model.User;

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
}
