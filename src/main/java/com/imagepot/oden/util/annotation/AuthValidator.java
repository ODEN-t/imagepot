package com.imagepot.oden.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.imagepot.oden.model.SigninForm;
import com.imagepot.oden.service.SigninFormService;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthValidator implements ConstraintValidator<Auth, SigninForm> {

    @Autowired
    SigninFormService signinFormService;

    @Override
    public void initialize(Auth constraintAnnotaion) {
    }

    @Override
    public boolean isValid(SigninForm form, ConstraintValidatorContext context) {
        Integer result = signinFormService.auth(form);

        // UPDATE件数が0の場合hasErrorをtrueにする
        if (result == null) {
            return true;
        }
        return false;
    }
}
