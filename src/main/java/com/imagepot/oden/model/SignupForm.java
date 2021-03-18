package com.imagepot.oden.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {
    @NotBlank
    @Length(min = 6, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String loginId;

    @NotBlank
    @Length(min = 8, max = 10)
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String password;

    @NotBlank
    @Length(min = 3, max = 10)
    private String userName;
}
