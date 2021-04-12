package com.imagepot.oden.service;

import java.util.List;

import com.imagepot.oden.model.User;
import com.imagepot.oden.repository.mybatis.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    // 特定のユーザ情報を全て取得
    public User selectOneUser(Integer id) {
        return userMapper.selectOneUser(id);
    }

    // 全ユーザの情報を取得
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    // 特定のユーザを削除
    public boolean deleteOneUser(Integer id) {
        return userMapper.deleteOneUser(id);
    }

    // 特定のユーザのアイコンを変更
    public boolean updateIcon(User user) {
        return userMapper.updateIcon(user);
    }

    // 特定のユーザログイン状況を更新
    public boolean updateLoggingIn(User user) {
        return userMapper.updateLoggingIn(user);
    }
}
