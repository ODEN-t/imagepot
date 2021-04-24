package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.AppUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController {

    @GetMapping("/settings")
    public String getSettings(Model model, @AuthenticationPrincipal AppUserDetails user) {
        return "settings";
    }

    @PostMapping(value = "/settings/upload/newicon", params = "newIcon")
    public String setNewIcon(Model model) {
        System.out.println(model);
        return "settings";
    }
}
