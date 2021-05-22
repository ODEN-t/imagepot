package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Slf4j
@Controller
@RequestMapping("/action")
public class ActionController {

    private final StorageService s3Service;
    private final MessageSource messageSource;

    @Autowired
    public ActionController(StorageService s3Service, MessageSource messageSource) {
        this.s3Service = s3Service;
        this.messageSource = messageSource;
    }

    @ModelAttribute
    List<Image> setImageList(@AuthenticationPrincipal LoginUser loginUser) {
        return s3Service.getObjList(loginUser);
    }

    @GetMapping
    public String getAction(
            @AuthenticationPrincipal LoginUser loginUser,
            List<Image> imageList,
            Model model) {

        // get result message from deleteUser();
        Optional.ofNullable(model.getAttribute("deleteError"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("deleteSuccess"))
                .ifPresent(model::addAttribute);
        Optional.ofNullable(model.getAttribute("message"))
                .ifPresent(model::addAttribute);

        model.addAttribute("images", imageList);
        return "action";
    }

    @PostMapping(value = "/delete", params = "delete")
    public String deleteImages(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(value = "imgData", required = false)String[] imgData,
            List<Image> imageList,
            Model model) {
        try {
            s3Service.deleteFile(imgData, loginUser);
        } catch (NullPointerException e) {
            e.printStackTrace();
            model.addAttribute("deleteError", true);
            model.addAttribute("message", messageSource.getMessage("error.deleteImage",null, Locale.ENGLISH));
            return "action";
        } finally {
            model.addAttribute("images", imageList);
        }

        model.addAttribute("deleteSuccess", true);
        model.addAttribute("message", messageSource.getMessage("success.deleteImage",null, Locale.ENGLISH));
        return "action";
    }
}
