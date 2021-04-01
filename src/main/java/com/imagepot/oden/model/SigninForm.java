package com.imagepot.oden.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SigninForm {
    @NotBlank(message = "Email or password not valid.")
    @Email(message = "Email or password not valid.")
    @Length(max = 255, message = "Email or password not valid.")
    private String email;

    @NotBlank(message = "Email or password not valid.")
    @Length(min = 8, max = 10, message = "Email or password not valid.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Email or password not valid.")
    private String password;
}
