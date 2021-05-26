package com.imagepot.xyztk.service;

import com.imagepot.xyztk.model.File;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // 全ファイルデータ挿入 (uploadFile in StorageService)
    public void insertFiles(List<File> files) {
        fileRepository.saveAll(files);
    }

    // ログインユーザのファイルデータ全て取得 (getObjList in StorageService)
    public List<File> getAllFilesByUserId(User user) {
        return fileRepository.selectFilesByUserId(user.getId());
    }

    // 特定のファイルデータ削除 (deleteFile in StorageService)
    public void deleteFilesByKey(List<File> files) {
        for(File f : files) {
            fileRepository.deleteAllByKey(f.getKey());
        }
    }

}
