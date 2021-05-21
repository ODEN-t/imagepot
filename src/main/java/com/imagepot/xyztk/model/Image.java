package com.imagepot.xyztk.model;

import lombok.Data;

import java.net.URL;
import java.util.Date;

@Data
public class Image {
    private String id;
    private URL url;
    private String title;
    private long size;
    private Date lastModified;
}
