package com.imagepot.oden.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.imagepot.oden.model.SigninForm;
import com.imagepot.oden.model.User;
import com.imagepot.oden.service.SigninFormService;
import com.imagepot.oden.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthValidator implements ConstraintValidator<Auth, SigninForm> {

    @Autowired
    SigninFormService signinFormService;

    @Autowired
    UserService userService;

    @Override
    public void initialize(Auth constraintAnnotaion) {
    }

    @Override
    public boolean isValid(SigninForm form, ConstraintValidatorContext context) {
        boolean result = signinFormService.auth(form);

        // 認証に失敗した場合
        if (!result) {
            return false;
        }

        // ログイン状況を更新
        User user = new User();
        user.setEmail(form.getSigninEmail());
        userService.updateLoggingIn(user);

        return true;
    }
}
