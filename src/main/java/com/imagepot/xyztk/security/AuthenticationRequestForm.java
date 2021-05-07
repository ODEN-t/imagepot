package com.imagepot.xyztk.security;

import lombok.Data;

@Data
public class AuthenticationRequestForm {
    private String email;
    private String password;
}
