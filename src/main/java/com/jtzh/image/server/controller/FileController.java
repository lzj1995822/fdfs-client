package com.jtzh.image.server.controller;

import com.jtzh.image.server.service.FdfsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FdfsServiceImpl fdfsService;

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        System.out.println("----开始处理请求-----");
        return fdfsService.upload(file);
    }

    @GetMapping("/hello")
    public String hell() {
        return  "hellow";
    }
}