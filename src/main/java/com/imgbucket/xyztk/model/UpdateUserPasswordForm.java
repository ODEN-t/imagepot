package com.imgbucket.xyztk.model;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class UpdateUserPasswordForm {

    @NotBlank(groups = UpdateUserPasswordFormValidations.Group1.class)
    private String currentPassword;

    @NotBlank(groups = UpdateUserPasswordFormValidations.Group2.class)
    @Size(min = 8, max = 10, groups = UpdateUserPasswordFormValidations.Group3.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = UpdateUserPasswordFormValidations.Group4.class)
    private String newPassword;

    private String confirmNewPassword;

    @AssertTrue(message = "New Password and Confirm New Password does not match.", groups = UpdateUserPasswordFormValidations.Group5.class)
    public boolean isValid() {
        return newPassword.equals(confirmNewPassword);
    }
}
