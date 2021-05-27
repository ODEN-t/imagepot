package com.imagepot.xyztk.service;

import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.model.LoginUser;
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
    public void insertFiles(List<PotFile> PotFiles) {
        fileRepository.saveAll(PotFiles);
    }

    // ログインユーザのファイルデータ全て取得 (getObjList in StorageService)
    public List<PotFile> getAllFilesById(LoginUser loginUser) {
        User u = new User();
        u.setId(loginUser.id);
        return fileRepository.selectFilesById(loginUser.id);
    }

    // 特定のファイルデータ削除 (deleteFile in StorageService)
    public void deleteFilesByKey(List<PotFile> PotFiles) {
        for(PotFile f : PotFiles) {
            fileRepository.deleteAllByKey(f.getKey());
        }
    }

}
