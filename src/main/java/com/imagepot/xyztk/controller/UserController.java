package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.RegistForm;
import com.imagepot.xyztk.model.ValidationAll;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("allusers")
    public String getUsers() {
//        List<User> users = List.of(
//                new User(
//                1L, "test", "test@gmail.com", "password", null, "ROLE_ADMIN",
//                null, 0, true, true, null,
//                null),
//                new User(
//                        1L, "test2", "test2@gmail.com", "password", null, "ROLE_USER",
//                        null, 0, true, true, null,
//                        null));

        return "userList";
    }

    @PostMapping(value = "signup")
    public String registUser(
            @ModelAttribute @Validated(ValidationAll.class) RegistForm registForm,
            BindingResult resultSignup,
            RedirectAttributes atts) {
        if (resultSignup.hasErrors()) {
            atts.addAttribute("hasErrors", true);
            return "top";
        }

//        User user = new User(id, name, email, password, icon, role, passwordUpdatedAt, signinMissTimes, unlock, enabled, createdAt, updatedAt);
//        user.setEmail(signupform.getSignupEmail());
//        user.setPassword(signupform.getSignupPassword());
//        user.setName(signupform.getName());
//        Integer result = signupFormService.registUser(user);
//        if (result == 1) {
//            System.out.println("insert成功");
//            model.addAttribute("registSuccess", true);
//        } else {
//            System.out.println("insert失敗");
//        }

        return "redirect:/";
    }
}
