//package com.imagepot.xyztk.util.annotation;
//
//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;
//
//
//import com.imagepot.xyztk.model.User;
//import com.imagepot.xyztk.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Optional;
//
//public class UnusedValidator implements ConstraintValidator<Unused, String> {
//
//    private final UserService userService;
//
//    @Autowired
//    public UnusedValidator(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void initialize(Unused constraintAnnotaion) {
//    }
//
//    // check the duplication of email
//    @Override
//    public boolean isValid(String email, ConstraintValidatorContext context) {
//        Optional<User> userOptional = userService.checkDuplicateUser(email);
//        return userOptional.isPresent();
//    }
//}
