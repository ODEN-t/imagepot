package com.imagepot.xyztk.service;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import com.imagepot.xyztk.model.UpdateUserPasswordForm;
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


    // SELECT
    public Optional<User> getUserByEmail(User user) throws IllegalStateException {
        return userRepository.selectUserByEmail(user.getEmail());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }


    // UPDATE
    public int updateIcon(long userId, byte[] icon) {
        return userRepository.updateUserIcon(userId, icon);
    }

    public int resetIcon(long userId) {
        return userRepository.resetUserIcon(userId);
    }

    public int updateName(User user) {
        return userRepository.updateUserName(user.getId(), user.getName());
    }

    public int updateEmail(User user) {
        Optional<User> result = userRepository.selectUserByEmail(user.getEmail());
        if(result.isPresent()) {
            throw new IllegalStateException("Email has already been taken.");
        }
        return userRepository.updateUserEmail(user.getId(), user.getEmail());
    }

    public int updatePassword(User user, String newPasswordEncoded) {
        return userRepository.updateUserPassword(user.getId(), newPasswordEncoded);
    }



    // INSERT
    public void addNewUser(User user) {
        Optional<User> userOptional = userRepository.selectUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("Email has already been taken.");
        }
        userRepository.save(user);
    }


    // DELETE
    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new IllegalStateException("User with ID " + userId + "does not exists");
        }
        userRepository.deleteById(userId);
    }

}
