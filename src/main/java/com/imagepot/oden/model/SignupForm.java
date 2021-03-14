package com.imagepot.oden.model;

import lombok.Data;

@Data
public class SignupForm {
    private String loginId;
    private String password;
    private String firstName;
    private String lastName;
    private String nickName;
}
