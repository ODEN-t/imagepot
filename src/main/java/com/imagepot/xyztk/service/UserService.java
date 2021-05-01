package com.imagepot.xyztk.service;

import java.util.List;
import java.util.Optional;

import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public boolean checkEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
