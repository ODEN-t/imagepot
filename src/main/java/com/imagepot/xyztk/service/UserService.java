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

    public Optional<User> getUserByEmail(String email) throws IllegalStateException {
        return Optional.ofNullable(userRepository
                .selectUserByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(String.format("Email %s not found.", email))));
    }

    public int updateIcon(long userId, byte[] icon) {
        return userRepository.updateUserIcon(userId, icon);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(User user) {
        Optional<User> userOptional = userRepository.selectUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("Email has already been taken.");
        }
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new IllegalStateException("User with ID " + userId + "does not exists");
        }
        userRepository.deleteById(userId);
    }

}
