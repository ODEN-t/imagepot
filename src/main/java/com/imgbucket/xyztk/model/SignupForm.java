package com.imgbucket.xyztk.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class SignupForm {
    @NotBlank(groups = SignupFormValidations.Group1.class)
    @Size(max = 20, groups = SignupFormValidations.Group2.class)
    private String name;

    public void setName(final String name) {
        this.name = name.strip();
    }

    @NotBlank(groups = SignupFormValidations.Group3.class)
    @Email(groups = SignupFormValidations.Group4.class)
    @Size(max = 255, groups = SignupFormValidations.Group5.class)
    private String email;

    @NotBlank(groups = SignupFormValidations.Group6.class)
    @Size(min = 8, max = 10, groups = SignupFormValidations.Group7.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", groups = SignupFormValidations.Group8.class)
    private String password;

    private String confirmPassword;

    @AssertTrue(message = "Password and Confirm Password does not match.", groups = SignupFormValidations.Group9.class)
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
