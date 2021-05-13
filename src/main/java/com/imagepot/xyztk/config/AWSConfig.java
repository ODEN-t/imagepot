package com.imagepot.xyztk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

// 独自プロパティ バケット名追加
@Component
@ConfigurationProperties(prefix = "aws.s3")
@Data
public class AWSConfig {
    private String bucketName;
    private String folderPrefix;
}
