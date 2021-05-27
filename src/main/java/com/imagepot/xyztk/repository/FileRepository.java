package com.imagepot.xyztk.repository;

import com.imagepot.xyztk.model.PotFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<PotFile, UUID> {
    @Query("SELECT f FROM PotFile f WHERE f.user_id.id = :id")
    public List<PotFile> selectFilesById(@Param("id") long id);

    @Query("DELETE FROM PotFile f WHERE f.key= :key")
    public void deleteAllByKey(@Param("key") String key);
}
