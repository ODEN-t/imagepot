package com.imgbucket.xyztk.service;

import com.imgbucket.xyztk.model.PotFile;
import com.imgbucket.xyztk.model.LoginUser;
import com.imgbucket.xyztk.repository.FileRepository;
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

    /**
     * ユーザのアップロードしたファイル情報をDBに登録する
     * @param PotFiles ユーザのアップロードしたファイル情報
     */
    public void insertFiles(List<PotFile> PotFiles) {
        fileRepository.saveAll(PotFiles);
    }

    /**
     * ログインユーザのファイルデータを全てDBから取得する
     * @param loginUser ログインユーザ情報
     * @return DBに存在するユーザのアップロードしたファイル情報
     */
    public List<PotFile> getAllFilesById(LoginUser loginUser) {
        return fileRepository.selectFilesById(loginUser.id);
    }

    /**
     * ユーザが選択したファイルをDBから削除する
     * @param PotFiles 削除対象のファイル情報
     */
    public void deleteFilesByKey(List<PotFile> PotFiles) {
        for(PotFile f : PotFiles) {
            fileRepository.deleteAllByKey(f.getKey());
        }
    }
}
