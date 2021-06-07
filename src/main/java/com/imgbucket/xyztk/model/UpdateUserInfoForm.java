package com.imgbucket.xyztk.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Data
public class UpdateUserInfoForm {

    @Size(max = 20, groups = UpdateUserInfoFormValidations.Group1.class, message = "{Size.userInfo.name}")
    private String name;
    public void setName(final String name) {
        this.name = name.strip();
    }

    @Email(groups = UpdateUserInfoFormValidations.Group2.class, message = "{Email}")
    @Size(max = 256, groups = UpdateUserInfoFormValidations.Group3.class, message = "{Size.userInfo.email}")
    private String email;
}
