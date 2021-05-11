package com.imagepot.xyztk.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class UserInfo {

    @Length(min = 2, max = 20, groups = SignupFormValidations.Group2.class)
    private String name;

    @Email(groups = SignupFormValidations.Group4.class)
    @Length(max = 255, groups = SignupFormValidations.Group5.class)
    private String email;
}
