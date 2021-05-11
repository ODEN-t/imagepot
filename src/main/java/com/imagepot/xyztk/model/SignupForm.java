package com.imagepot.xyztk.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.imagepot.xyztk.model.SignupFormValidations.*;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {
    @NotBlank(groups = Group1.class)
    @Length(min = 2, max = 20, groups = Group2.class)
    private String name;

    public void setName(final String name) {
        this.name = name.strip();
    }

    @NotBlank(groups = Group3.class)
    @Email(groups = Group4.class)
    @Length(max = 255, groups = Group5.class)
    private String email;

    @NotBlank(groups = Group6.class)
    @Length(min = 8, max = 10, groups = Group7.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group8.class)
    private String password;

    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password does not match.", groups = Group9.class)
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
