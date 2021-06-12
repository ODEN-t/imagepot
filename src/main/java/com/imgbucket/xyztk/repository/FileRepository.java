package com.imgbucket.xyztk.repository;

import com.imgbucket.xyztk.model.BktFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<BktFile, UUID> {
    @Query("SELECT f FROM BktFile f WHERE f.user.id = :id order by f.lastModifiedAt desc")
    public List<BktFile> selectFilesById(@Param("id") long id);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BktFile f WHERE f.key= :key")
    public void deleteAllByKey(@Param("key") String key);
}