package com.imgbucket.xyztk.service;

import com.imgbucket.xyztk.model.BktFile;
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
     * @param bktFiles ユーザのアップロードしたファイル情報
     */
    public void insertFiles(List<BktFile> bktFiles) {
        fileRepository.saveAll(bktFiles);
    }

    /**
     * ログインユーザのファイルデータを全てDBから取得する
     * @param loginUser ログインユーザ情報
     * @return DBに存在するユーザのアップロードしたファイル情報
     */
    public List<BktFile> getAllFilesById(LoginUser loginUser) {
        return fileRepository.selectFilesById(loginUser.id);
    }

    /**
     * ユーザが選択したファイルをDBから削除する
     * @param bktFiles 削除対象のファイル情報
     */
    public void deleteFilesByKey(List<BktFile> bktFiles) {
        for(BktFile f : bktFiles) {
            fileRepository.deleteAllByKey(f.getKey());
        }
    }
}
