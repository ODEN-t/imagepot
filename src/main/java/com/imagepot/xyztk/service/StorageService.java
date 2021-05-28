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

    // クライアント経由でs3を操作
    private final AmazonS3 s3Cliant;
    private final UtilComponent utilComponent;


    @Autowired
    public StorageService(AmazonS3 s3Cliant, UtilComponent utilComponent) {
        this.s3Cliant = s3Cliant;
        this.utilComponent = utilComponent;
    }


    public List<PotFile> getObjList(LoginUser loginUser) {
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName).withPrefix(pathToUserFolder);
        ListObjectsV2Result result = s3Cliant.listObjectsV2(request);

        List<PotFile> fileList = new ArrayList<>();

        for(S3ObjectSummary objList : result.getObjectSummaries()) {
            if(objList.getSize() <= 0) continue;
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
     * @param images MultipartFileを格納したリスト
     * @param loginUser ログインユーザ情報
     * @return 文字列
     */
    public List<PotFile> getUploadedFilesAfterUpload(ArrayList<MultipartFile> images, LoginUser loginUser) {

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

        User u = new User();
        u.setId(loginUser.id);
        List<PotFile> uploadedFileList = new ArrayList<>();
        for(String key : uploadedKeyList) {
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

    public ResponseEntity<byte[]> downloadFile(String[] checkedFileNameList, LoginUser loginUser) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String key : checkedFileNameList) {
                S3Object s3Object = s3Cliant.getObject(bucketName, key);
                byte[] data = IOUtils.toByteArray(s3Object.getObjectContent());

                // フォルダ以下のファイル名のみを指定
                ZipEntry zipEntry = new ZipEntry(key.substring(key.lastIndexOf('/') + 1));
                zos.putNextEntry(zipEntry);
                zos.write(data);
                zos.closeEntry();
            }
        } catch (IOException | NullPointerException | AmazonServiceException e){
            e.printStackTrace();
        }
        byte[] responseData = baos.toByteArray();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.zip");
        return new ResponseEntity<>(responseData, httpHeaders, HttpStatus.OK);
    }

    public List<PotFile> getDeleteListAfterDeleteFiles(String[] fileKeyList) {
        List<PotFile> deleteList = new ArrayList<>();
        for(String key : fileKeyList) {
            s3Cliant.deleteObject(bucketName, key);
            PotFile f = new PotFile();
            f.setKey(key);
            deleteList.add(f);
            log.info("Delete image :" + key);
        }

        return deleteList;
    }

    /*
     * MultipartFile ファイルのアップロード機能をアプリケーションコード内で透過的に扱うためのクラス
     * マルチパートリクエストで受信したアップロードファイルを扱う
     */
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
