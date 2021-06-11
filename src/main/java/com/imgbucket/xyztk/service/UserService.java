package com.imgbucket.xyztk.service;

import java.util.List;
import java.util.Optional;

import com.imgbucket.xyztk.model.LoginUser;
import com.imgbucket.xyztk.model.User;
import com.imgbucket.xyztk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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


    /**
     * 登録済み全ユーザをDBから取得する
     * @return 全ユーザ情報のリスト
     */
    public List<User> getUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    /**
     * ユーザアイコンを更新する
     * @param loginUser ログイン中のユーザ情報
     * @param icon ユーザがアップロードしたアイコンのバイト配列
     */
    public void updateIcon(LoginUser loginUser, byte[] icon) {
        userRepository.updateUserIcon(loginUser.id, icon);
    }


    /**
     * DBに登録されているアイコン画像をnullに更新する
     *
     * @param loginUser ユーザ情報
     */
    public void resetIcon(LoginUser loginUser) {
        userRepository.resetUserIcon(loginUser.id);
    }


    /**
     * ユーザ名を更新する
     *
     * @param user ユーザ情報
     */
    public void updateName(User user) {
        userRepository.updateUserName(user.getId(), user.getName());
    }


    /**
     * ユーザのメールアドレスを更新する
     *
     * @param user ユーザ情報
     * @throws IllegalStateException 登録済みのメールアドレスの場合
     */
    public void updateEmail(User user) {
        Optional<User> result = userRepository.selectUserByEmail(user.getEmail());
        if (result.isPresent()) {
            throw new IllegalStateException("Email has already been taken.");
        }
        userRepository.updateUserEmail(user.getId(), user.getEmail());
    }

    /**
     * ユーザのパスワードを更新する
     *
     * @param user               ユーザ情報
     * @param newPasswordEncoded 暗号化したパスワード
     */
    public void updatePassword(User user, String newPasswordEncoded) {
        userRepository.updateUserPassword(user.getId(), newPasswordEncoded);
    }


    /**
     * ユーザ情報をDBに登録する
     *
     * @param user ユーザ情報
     * @throws IllegalStateException 登録済みのメールアドレスの場合
     */
    public void insertNewUser(User user) {
        Optional<User> userOptional = userRepository.selectUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("Email has already been taken.");
        }
        userRepository.save(user);
    }


    /**
     * ユーザ情報をDBから削除する
     *
     * @param userId ユーザ情報
     * @throws IllegalStateException 存在しないユーザ情報の場合
     */
    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with ID " + userId + "does not exists");
        }
        userRepository.deleteById(userId);
    }

}
