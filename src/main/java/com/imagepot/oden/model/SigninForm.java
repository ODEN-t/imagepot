package com.imagepot.oden.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import com.imagepot.oden.model.ValidationGroups.*;

import lombok.Data;

@Data
public class SigninForm {
    @NotBlank(groups = Group11.class)
    @Email(groups = Group12.class)
    @Length(max = 255, groups = Group13.class)
    private String signinEmail;

    @NotBlank(groups = Group14.class)
    @Length(min = 8, max = 10, groups = Group15.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group16.class)
    private String signinPassword;
}
