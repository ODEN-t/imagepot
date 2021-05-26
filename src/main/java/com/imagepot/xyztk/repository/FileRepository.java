package com.imagepot.xyztk.repository;

import com.imagepot.xyztk.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Character> {
    @Query("SELECT f FROM File f WHERE f.user_id = ?1")
    public List<File> selectFilesByUserId(long id);

    @Query("DELETE FROM File f WHERE f.key= :key")
    public void deleteAllByKey(@Param("key") String key);
}
