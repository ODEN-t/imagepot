package com.imagepot.xyztk.model;

import lombok.Data;

import com.imagepot.xyztk.model.UpdateUserInfoFormValidations.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


@Data
public class UpdateUserInfoForm {

    @Size(max = 20, groups = Group1.class, message = "{Size.userInfo.name}")
    private String name;
    public void setName(final String name) {
        this.name = name.strip();
    }

    @Email(groups = Group2.class, message = "{Email}")
    @Size(max = 256, groups = Group3.class, message = "{Size.userInfo.email}")
    private String email;
}
