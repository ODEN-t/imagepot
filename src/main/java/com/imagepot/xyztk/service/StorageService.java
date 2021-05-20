package com.imagepot.xyztk.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.util.IOUtils;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.User;
import com.imagepot.xyztk.util.XferMgrProgress;
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

    @Autowired
    public StorageService(AmazonS3 s3Cliant) {
        this.s3Cliant = s3Cliant;
    }

    public Image getObjList(LoginUser loginUser) {
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName).withPrefix(pathToUserFolder);
        ListObjectsV2Result result = s3Cliant.listObjectsV2(request);

        List<URL> imageUrlList = new ArrayList<>();
        Image images = new Image();

        for(S3ObjectSummary objList : result.getObjectSummaries()) {
            if(objList.getSize() <= 0) continue;
            imageUrlList.add(s3Cliant.getUrl(bucketName, objList.getKey())); //image url
        }

        images.setUrlList(imageUrlList);
        return images;
    }

    public String uploadFile(ArrayList<MultipartFile> multipartFileList, LoginUser loginUser) {
        ArrayList<File> fileList = testConvert(multipartFileList);

        TransferManager xfer_mgr = TransferManagerBuilder.standard()
                .withS3Client(s3Cliant)
                .build();

        try {
//            InputStream multipartFileInStream = multipartFile.getInputStream();
            String folderPrefix = "potuser";
            String filePath = folderPrefix + loginUser.id + "/";

            MultipleFileUpload xfer = xfer_mgr.uploadFileList(bucketName, filePath, new File("."), fileList);
            XferMgrProgress.showMultiUploadProgress(xfer);

//            String fileName = formatNow + "_" + multipartFile.getOriginalFilename();
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(multipartFile.getSize());
//
//            s3Cliant.putObject(new PutObjectRequest(bucketName, filePath + fileName, multipartFileInStream, objectMetadata));
//            fileObj.delete();
        } catch (Exception e) {
            e.printStackTrace();
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

    private ArrayList<File> testConvert(ArrayList<MultipartFile> multipartFileList) {
        ArrayList<File> convertedFileList = new ArrayList<>();

        for(MultipartFile m : multipartFileList) {

            // 現在日時の取得
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String formatNow = formatter.format(now);

            File newFile = new File(formatNow + "_" + Objects.requireNonNull(m.getOriginalFilename()));

            try(FileOutputStream fos = new FileOutputStream(newFile)) {
                fos.write(m.getBytes());
                convertedFileList.add(newFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertedFileList;
    }
}
