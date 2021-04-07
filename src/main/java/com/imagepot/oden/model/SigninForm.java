package com.imagepot.oden.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import com.imagepot.oden.model.ValidationGroups.*;
import com.imagepot.oden.util.annotation.Auth;
import com.imagepot.oden.util.annotation.Exist;

import lombok.Data;

@Data
@Auth(groups = Group18.class)
public class SigninForm {
    @NotBlank(groups = Group11.class)
    @Email(groups = Group12.class)
    @Length(max = 255, groups = Group13.class)
    @Exist(groups = Group14.class)
    private String signinEmail;

    @NotBlank(groups = Group15.class)
    @Length(min = 8, max = 10, groups = Group16.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group17.class)
    private String signinPassword;
}
