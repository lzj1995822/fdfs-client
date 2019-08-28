package com.jtzh.image.server.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class FdfsServiceImpl {

    @Autowired
    private FastFileStorageClient storageClient;

    private String uploadFile(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = storageClient.uploadFile(byteArrayInputStream, fileSize, extension, null);
        System.out.println(storePath.getGroup() + "===" + storePath.getPath() + "======" + storePath.getFullPath());
        return storePath.getFullPath();
    }

    public String upload(MultipartFile file) {
        Date start = new Date();
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        try {
            String path = uploadFile(file.getBytes(), file.getSize(), extension);
            System.out.print("上传消耗时长:");
            System.out.println(start.getTime() - new Date().getTime());
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
