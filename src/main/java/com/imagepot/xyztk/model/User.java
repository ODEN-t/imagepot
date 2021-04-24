package com.imagepot.xyztk.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private byte[] icon;
    private String role;
    private Timestamp passwordUpdatedAt;
    private Integer signinMissTimes;
    private boolean unlock;
    private boolean enabled;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
