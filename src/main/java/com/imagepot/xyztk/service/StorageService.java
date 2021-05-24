package com.imagepot.xyztk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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


    /**
     * ユーザフォルダ内の画像を全て取得しリストで返す
     * @param loginUser ログインユーザ情報
     * @return Image型のリスト
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
            images.setRowSize(objList.getSize());
            images.setSize(utilComponent.readableSize(objList.getSize()));
            images.setLastModified(objList.getLastModified());
            images.setUrl(s3Cliant.getUrl(bucketName, objList.getKey()));
            images.setChecked(false);
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
                String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";

                // 現在日時の取得
                LocalDateTime now = LocalDateTime.now();
                String nowFormatted = DateTimeFormatter
                        .ofPattern("HHmmssSSS")
                        .format(now);

                String fileName = nowFormatted + "_" + multipartFile.getOriginalFilename();
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());

                s3Cliant.putObject(new PutObjectRequest(bucketName, pathToUserFolder + fileName, multipartFileInStream, objectMetadata));
                fileObj.delete();
            } catch (IOException | AmazonServiceException e) {
                e.printStackTrace();
            }
        }
        return "File uploaded";
    }

    public ResponseEntity<byte[]> downloadFile(String[] checkedFileNameList, LoginUser loginUser) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String checkedFileName : checkedFileNameList) {
                String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";

                S3Object s3Object = s3Cliant.getObject(bucketName, pathToUserFolder + checkedFileName);
                byte[] data = IOUtils.toByteArray(s3Object.getObjectContent());

                ZipEntry zipEntry = new ZipEntry(checkedFileName);
                zos.putNextEntry(zipEntry);
                zos.write(data);
                zos.closeEntry();
            }
        } catch (IOException | NullPointerException e){
            e.printStackTrace();
        }
        byte[] responseData = baos.toByteArray();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.zip");
        return new ResponseEntity<>(responseData, httpHeaders, HttpStatus.OK);
    }

    public void deleteFile(String[] fileNameList, LoginUser loginUser) {
        String pathToUserFolder = folderPrefix + Long.toString(loginUser.id) + "/";
        int count = 0;

        for(String fileName : fileNameList) {
            s3Cliant.deleteObject(bucketName, pathToUserFolder + fileName);
            log.info("Delete image :" + pathToUserFolder + fileName);
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
