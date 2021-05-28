package com.imagepot.xyztk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class StorageService {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.folderPrefix}")
    private String folderPrefix;

    private final AmazonS3 s3Cliant;

    @Autowired
    public StorageService(AmazonS3 s3Cliant) {
        this.s3Cliant = s3Cliant;
    }


    public List<PotFile> getObjList(LoginUser loginUser) {
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName).withPrefix(pathToUserFolder);
        ListObjectsV2Result result = s3Cliant.listObjectsV2(request);

        List<PotFile> fileList = new ArrayList<>();

        for (S3ObjectSummary objList : result.getObjectSummaries()) {
            if (objList.getSize() <= 0) continue;
            PotFile f = new PotFile();
            f.setKey(objList.getKey());
            f.setUrl(s3Cliant.getUrl(bucketName, objList.getKey()));
            f.setName(objList.getKey().substring(pathToUserFolder.length()));
            f.setSize(objList.getSize());
            f.setType(objList.getKey().substring(pathToUserFolder.length()).substring(objList.getKey().substring(pathToUserFolder.length()).lastIndexOf(".") + 1));
            f.setLastModifiedAt((Timestamp) objList.getLastModified());

            fileList.add(f);
        }
        return fileList;
    }

    /**
     * MultipartFileをリネームしバケット内のユーザフォルダに保存する
     *
     * @param images    MultipartFileを格納したリスト
     * @param loginUser ログインユーザ情報
     * @return アップロードが成功したファイル情報を詰めたリスト
     */
    public List<PotFile> s3UploadFile(ArrayList<MultipartFile> images, LoginUser loginUser) {

        List<String> uploadedKeyList = new ArrayList<>();
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";

        for (MultipartFile multipartFile : images) {
            try {
                InputStream multipartFileInStream = multipartFile.getInputStream();

                // 現在日時の取得
                LocalDateTime now = LocalDateTime.now();
                String nowFormatted = DateTimeFormatter
                        .ofPattern("HHmmssSSS")
                        .format(now);

                String fileName = nowFormatted + "_" + multipartFile.getOriginalFilename();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());

                s3Cliant.putObject(new PutObjectRequest(bucketName, pathToUserFolder + fileName, multipartFileInStream, objectMetadata));
                uploadedKeyList.add(pathToUserFolder + fileName);
            } catch (IOException | AmazonServiceException e) {
                e.printStackTrace();
            }
        }

        // アップロードしたファイル情報をユーザ情報と一緒に返す
        User u = new User();
        u.setId(loginUser.id);
        List<PotFile> uploadedFileList = new ArrayList<>();
        for (String key : uploadedKeyList) {
            S3Object obj = s3Cliant.getObject(new GetObjectRequest(bucketName, key));
            PotFile f = new PotFile();
            f.setFile_id(UUID.randomUUID());
            f.setUser_id(u);
            f.setKey(obj.getKey());
            f.setUrl(s3Cliant.getUrl(bucketName, obj.getKey()));
            f.setName(obj.getKey().substring(pathToUserFolder.length()));
            f.setSize(s3Cliant.getObjectMetadata(bucketName, key).getContentLength());
            f.setType(obj.getKey().substring(pathToUserFolder.length()).substring(obj.getKey().substring(pathToUserFolder.length()).lastIndexOf(".") + 1));
            Date lastModified = s3Cliant.getObjectMetadata(bucketName, key).getLastModified();
            f.setLastModifiedAt(new Timestamp(lastModified.getTime()));

            uploadedFileList.add(f);
        }
        return uploadedFileList;
    }

    /**
     * チェックボックスで選択したデータをs3からzip化してダウンロード
     * @param checkedFileNameList s3オブジェクトへのアクセスに必要なkeyのリスト
     * @param loginUser           ログインユーザ情報
     * @return zipファイル
     * @throws NullPointerException   keyのリストがNull(画面でチェックなし)の場合エラー
     * @throws IOException ファイル作成過程でのエラー
     * @throws AmazonServiceException s3側での重大なエラーの場合
     */
    public ResponseEntity<byte[]> s3DownloadFile(String[] checkedFileNameList, LoginUser loginUser)
            throws NullPointerException, IOException, AmazonServiceException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ZipOutputStream zos = new ZipOutputStream(baos);
        for (String key : checkedFileNameList) {
            S3Object s3Object = s3Cliant.getObject(bucketName, key);
            byte[] data = IOUtils.toByteArray(s3Object.getObjectContent());

            // フォルダ以下のファイル名のみを指定
            ZipEntry zipEntry = new ZipEntry(key.substring(key.lastIndexOf('/') + 1));
            zos.putNextEntry(zipEntry);
            zos.write(data);
            zos.closeEntry();
        }

        byte[] responseData = baos.toByteArray();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.zip");
        return new ResponseEntity<>(responseData, httpHeaders, HttpStatus.OK);
    }


    /**
     * チェックボックスで選択したデータをs3から削除する
     * @param fileKeyList 削除対象データのs3のkey
     * @return 削除データの情報を詰めたリスト
     */
    public List<PotFile> s3DeleteFile(String[] fileKeyList) {
        List<PotFile> deleteList = new ArrayList<>();
        for (String key : fileKeyList) {
            s3Cliant.deleteObject(bucketName, key);
            PotFile f = new PotFile();
            f.setKey(key);
            deleteList.add(f);
            log.info("Delete image :" + key);
        }

        return deleteList;
    }
}
