package com.imagepot.xyztk.model;

import com.imagepot.xyztk.model.SignupFormValidations.*;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignupForm {
    @NotBlank(groups = Group1.class)
    @Size(max = 20, groups = Group2.class)
    private String name;

    public void setName(final String name) {
        this.name = name.strip();
    }

    @NotBlank(groups = Group3.class)
    @Email(groups = Group4.class)
    @Size(max = 255, groups = Group5.class)
    private String email;

    @NotBlank(groups = Group6.class)
    @Size(min = 8, max = 10, groups = Group7.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = Group8.class)
    private String password;

    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password does not match.", groups = Group9.class)
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
