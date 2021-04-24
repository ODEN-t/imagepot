package com.imagepot.xyztk.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.imagepot.xyztk.service.SignupFormService;

import org.springframework.beans.factory.annotation.Autowired;

public class UnusedValidator implements ConstraintValidator<Unused, String> {

    @Autowired
    SignupFormService signupFormService;

    @Override
    public void initialize(Unused constraintAnnotaion) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        // 入力されたemailに重複があればtrueが返却される
        boolean result = signupFormService.checkDuplicateEmail(email);
        if (result) {
            return false;
        }
        return true;
    }
}
