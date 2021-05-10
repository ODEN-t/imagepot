package com.imagepot.xyztk.repository;

import com.imagepot.xyztk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public Optional<User> selectUserByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.icon = :iconData WHERE u.id = :userId")
    public int updateUserIcon(@Param("userId") long userId, @Param("iconData") byte[] icon);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.icon = null WHERE u.id = :userId")
    public int resetUserIcon(@Param("userId") long userId);
}
