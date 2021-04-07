package com.imagepot.oden.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.imagepot.oden.service.SignupFormService;

import org.springframework.beans.factory.annotation.Autowired;

public class UnusedValidator implements ConstraintValidator<Unused, String> {

    @Autowired
    SignupFormService signupFormService;

    @Override
    public void initialize(Unused constraintAnnotaion) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {

        // 入力されたemailに重複があれば1が返却される
        Integer result = signupFormService.checkDuplicateEmail(email);
        if (result == 0) {
            return true;
        }
        return false;
    }
}
