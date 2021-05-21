package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.Image;
import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/action")
public class ActionController {

    private final StorageService s3Service;

    @Autowired
    public ActionController(StorageService s3Service) {
        this.s3Service = s3Service;
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
        log.info(loginUser.toString());
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
            return "action";
        } finally {
            model.addAttribute("images", imageList);
        }
        return "action";
    }
}
