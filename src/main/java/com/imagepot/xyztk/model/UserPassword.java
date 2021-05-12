package com.imagepot.xyztk.model;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.imagepot.xyztk.model.UserPasswordValidations.*;


@Data
public class UserPassword {

    @NotBlank(groups = Group1.class)
    private String currentPassword;

    @NotBlank(groups = Group2.class)
    @Size(min = 8, max = 10, groups = Group3.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group4.class)
    private String newPassword;

    private String confirmNewPassword;

    @AssertTrue(message = "New Password and Confirm New Password does not match.", groups = Group5.class)
    public boolean isValid() {
        return newPassword.equals(confirmNewPassword);
    }
}
