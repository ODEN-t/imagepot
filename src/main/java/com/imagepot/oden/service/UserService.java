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

    public User selectOne(Integer id) {
        return userMapper.selectOne(id);
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }
}
