package com.imagepot.xyztk.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

import com.imagepot.xyztk.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    // クライアント経由でs3を操作
    private final AmazonS3 s3Cliant;

    @Autowired
    public StorageService(AmazonS3 s3Cliant) {
        this.s3Cliant = s3Cliant;
    }

    public Image getObjList() {
        ObjectListing objListing = s3Cliant.listObjects(bucketName);
        List<S3ObjectSummary> objList = objListing.getObjectSummaries();
        Image images = new Image();
        List<URL> imageUrlList = new ArrayList<>();

        for (S3ObjectSummary obj : objList) {
//             System.out.println(obj.getBucketName() + " / " + obj.getETag() + " / " + obj.getStorageClass()+ " / " + obj.getOwner());
//             System.out.println(obj.toString());
             imageUrlList.add(s3Cliant.getUrl(bucketName, obj.getKey()));//image url
//             System.out.println("Key [" + obj.getKey() + "] / Size [" + obj.getSize() + " B] / Last Modified [" + obj.getLastModified() + "]");
        }
        images.setUrlList(imageUrlList);
        return images;
    }

    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Cliant.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileName;
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

    public String deleteFile(String fileName) {
        s3Cliant.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
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
