package com.imagepot.xyztk.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.util.UtilComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

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


    /**
     * ユーザフォルダ内の画像を全て取得しリストで返す
     * @param loginUser ログインユーザ情報
     * @return List<Image> Image型のリスト
     */
    public List<Image> getObjList(LoginUser loginUser) {
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName).withPrefix(pathToUserFolder);
        ListObjectsV2Result result = s3Cliant.listObjectsV2(request);

        List<Image> imageList = new ArrayList<>();

        for(S3ObjectSummary objList : result.getObjectSummaries()) {
            if(objList.getSize() <= 0) continue;
            Image images = new Image();
            images.setId(UUID.randomUUID().toString());
            images.setTitle(objList.getKey().substring(pathToUserFolder.length()));
            images.setSize(utilComponent.readableSize(objList.getSize()));
            images.setLastModified(objList.getLastModified());
            images.setUrl(s3Cliant.getUrl(bucketName, objList.getKey()));
            imageList.add(images);
        }
        return imageList;
    }

    /**
     * MultipartFileをリネームしバケット内のユーザフォルダに保存する
     * @param images MultipartFileを格納したリスト
     * @param loginUser ログインユーザ情報
     * @return 文字列
     */
    public String uploadFile(ArrayList<MultipartFile> images, LoginUser loginUser) {

        for (MultipartFile multipartFile : images) {
            File fileObj = convertMultiPartFileToFile(multipartFile);

            try {
                InputStream multipartFileInStream = multipartFile.getInputStream();
                String folderPrefix = "potuser";
                String filePath = folderPrefix + loginUser.id + "/";

                // 現在日時の取得
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
                String formatNow = formatter.format(now);

                String fileName = formatNow + "_" + multipartFile.getOriginalFilename();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());

                s3Cliant.putObject(new PutObjectRequest(bucketName, filePath + fileName, multipartFileInStream, objectMetadata));
                fileObj.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "File uploaded";
    }

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Cliant.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteFile(String[] pathList, LoginUser loginUser) {
        String folderPrefix = "potuser";
        String filePath = folderPrefix + loginUser.id + "/";
        int count = 0;

        for(String path : pathList) {
            s3Cliant.deleteObject(bucketName, filePath + path);
            log.info("Delete image :" + filePath + path);
            count++;
        }
        log.info("Deleted " + count + " images");
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
