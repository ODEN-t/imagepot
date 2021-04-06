package com.imagepot.oden.repository.mybatis;

import java.util.List;

import com.imagepot.oden.model.User;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 登録用メソッド
    public boolean insert(User user);

    // 1件検索用メソッド
    public User selectOne(Integer id);

    // 全件検索用メソッド
    public List<User> selectAll();

    // 更新用メソッド
    public boolean updateOne(User user);

    // 1件削除用メソッド
    public boolean deleteOne(String userId);
}
