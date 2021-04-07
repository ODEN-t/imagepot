package com.imagepot.oden.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import com.imagepot.oden.model.ValidationGroups.*;
import com.imagepot.oden.util.annotation.Unused;

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
    @Unused(groups = Group6.class)
    private String signupEmail;

    @NotBlank(groups = Group7.class)
    @Length(min = 8, max = 10, groups = Group8.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group9.class)
    private String signupPassword;

    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password does not match.", groups = Group10.class)
    public boolean isConfirmeValid() {
        if (signupPassword.equals(confirmPassword)) {
            return true;
        }
        return false;
    }
}
