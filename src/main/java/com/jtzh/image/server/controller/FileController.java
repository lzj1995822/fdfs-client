package com.jtzh.image.server.controller;

import com.jtzh.image.server.conf.AccessTokenInterceptor;
import com.jtzh.image.server.service.FdfsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class FileController {

    @Autowired
    private FdfsServiceImpl fdfsService;

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    @PostMapping("/upload")
    public String upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        System.out.println("----开始处理请求-----");
        if (accessTokenInterceptor.preHandle(request, response)) {
           return fdfsService.upload(file);
        }
        return null;
    }

    @GetMapping("/hello")
    public String hell() {
        return  "hellow";
    }
}