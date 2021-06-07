package com.imagepot.xyztk.controller;

import com.imagepot.xyztk.model.LoginUser;
import com.imagepot.xyztk.model.PotFile;
import com.imagepot.xyztk.service.FileService;
import com.imagepot.xyztk.util.UtilComponent;
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
        log.info(loginUser.toString());
        List<URL> urlList = new ArrayList<>();
        for(PotFile potFile : potFileList) {
            urlList.add(potFile.getTmb_url());
        }
        String totalSizeReadable = utilComponent.readableSize(getTotalFileSize(potFileList));
        model.addAttribute("totalFiles", urlList.size());
        model.addAttribute("totalSizeReadable", totalSizeReadable);
        model.addAttribute("urlList", urlList);
        return "home";
    }

    /**
     * ファイル情報を詰めたリストから合計ファイルサイズを計算する
     * @param fileList ユーザが保持する全ファイル情報
     * @return 合計ファイルサイズ
     */
    public double getTotalFileSize(List<PotFile> fileList) {
        double totalSize = 0;
        for (PotFile potFile : fileList) {
            totalSize += potFile.getSize();
        }
        return totalSize;
    }
}
