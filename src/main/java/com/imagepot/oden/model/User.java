package com.imagepot.oden.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private byte[] icon;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean loggingIn;
}


///////????
