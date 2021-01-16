package com.imagepot.oden.login.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Users {
    private String userId;
    private String userPass;
    private String firstname;
    private String lastname;
    private Timestamp createDate;
    private Timestamp lastAccessDate;
    private int recStatus;
    private boolean passed;
}
