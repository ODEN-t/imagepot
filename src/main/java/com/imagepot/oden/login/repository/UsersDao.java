package com.imagepot.oden.login.repository;

import com.imagepot.oden.login.model.Users;

import org.springframework.dao.DataAccessException;

public interface UsersDao {
    // Userテーブルのデータを1件取得
    public Users selectOne(String userId) throws DataAccessException;
}
