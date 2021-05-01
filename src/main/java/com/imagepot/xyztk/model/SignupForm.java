package com.imagepot.xyztk.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.imagepot.xyztk.model.ValidationGroups.*;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {
    @NotBlank(groups = Group1.class)
    @Length(min = 2, max = 10, groups = Group2.class)
    private String name;

    public void setName(final String name) {
        this.name = name.strip();
    }

    @NotBlank(groups = Group3.class)
    @Email(groups = Group4.class)
    @Length(max = 255, groups = Group5.class)
    //@Unused(groups = Group6.class)
    private String email;

    @NotBlank(groups = Group5.class)
    @Length(min = 8, max = 10, groups = Group6.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group7.class)
    private String password;

    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password does not match.", groups = Group8.class)
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
