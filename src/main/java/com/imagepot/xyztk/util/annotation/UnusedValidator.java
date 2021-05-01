package com.imagepot.xyztk.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


import com.imagepot.xyztk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UnusedValidator implements ConstraintValidator<Unused, String> {

    private final UserService userService;

    @Autowired
    public UnusedValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(Unused constraintAnnotaion) {
    }

    // check the duplication of email
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean result = userService.checkEmail(email);
        return !result;
    }
}
