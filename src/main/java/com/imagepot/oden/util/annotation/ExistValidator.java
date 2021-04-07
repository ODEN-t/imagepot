package com.imagepot.oden.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.imagepot.oden.service.SigninFormService;

import org.springframework.beans.factory.annotation.Autowired;

public class ExistValidator implements ConstraintValidator<Exist, String> {

    @Autowired
    SigninFormService signinFormService;

    @Override
    public void initialize(Exist constraintAnnotaion) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean result = signinFormService.checkEmailForSignin(email);
        if (result) {
            return true;
        }
        return false;
    }
}
