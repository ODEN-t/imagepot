package com.imagepot.xyztk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImagepotApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImagepotApplication.class, args);
    }
}
