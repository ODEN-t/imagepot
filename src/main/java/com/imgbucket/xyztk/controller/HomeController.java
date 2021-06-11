package com.imgbucket.xyztk.controller;

import com.imgbucket.xyztk.model.LoginUser;
import com.imgbucket.xyztk.model.PotFile;
import com.imgbucket.xyztk.service.FileService;
import com.imgbucket.xyztk.util.UtilComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UtilComponent utilComponent;

    @Autowired
    public HomeController(FileService fileService, UtilComponent utilComponent) {
        this.fileService = fileService;
        this.utilComponent = utilComponent;
    }

    @ModelAttribute
    List<PotFile> getFileList(@AuthenticationPrincipal LoginUser loginUser) {
        return fileService.getAllFilesById(loginUser);
    }

    /**
     * Home画面を表示する
     * @param loginUser ログインユーザ情報
     * @param potFileList ユーザが保持する全ファイル情報
     * @return View
     */
    @GetMapping
    public String getHome(
            @AuthenticationPrincipal LoginUser loginUser,
            List<PotFile> potFileList,
            Model model) {
        List<URL> urlList = new ArrayList<>();
        for(PotFile potFile : potFileList) {
            urlList.add(potFile.getTmb_url());
        }
        model.addAttribute("totalFiles", urlList.size());
        model.addAttribute("totalSizeReadable", utilComponent.getReadableTotalSize(potFileList));
        model.addAttribute("urlList", urlList);
        return "home";
    }
}
