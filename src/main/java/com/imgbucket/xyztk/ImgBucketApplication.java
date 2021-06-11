package com.imgbucket.xyztk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImgBucketApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImgBucketApplication.class, args);
    }
}
