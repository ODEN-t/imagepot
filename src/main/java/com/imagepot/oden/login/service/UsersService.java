package com.imagepot.oden.login.service;

import com.imagepot.oden.login.model.Users;
import com.imagepot.oden.login.repository.UsersDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    @Qualifier("UsersDaoJdbcImpl")
    UsersDao dao;

    public Users selectOne(String userId) {
        return dao.selectOne(userId);
    }
}
