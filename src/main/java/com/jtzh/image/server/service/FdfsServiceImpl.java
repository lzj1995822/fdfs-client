package com.jtzh.image.server.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FdfsServiceImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FastFileStorageClient storageClient;

    /** redis 数据库操作模板类*/
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //待上传的图片文件数量
    private Integer uploadAmount = 0;

    private static final String REDIS_IMG_URL = "http://122.97.218.162:21018/api/identity/accessory/getImage/";

    private static final String FDFS_IMG_URL = "http://122.97.218.162:8888/";

    private static final Integer MAX_UPLOAD_AMOUNT = 50;

    private String uploadFile(byte[] bytes, long fileSize, String extension) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = storageClient.uploadFile(byteArrayInputStream, fileSize, extension, null);
        System.out.println(storePath.getGroup() + "===" + storePath.getPath() + "======" + storePath.getFullPath());
        return storePath.getFullPath();
    }

    public String upload(MultipartFile file) {
        uploadAmount++;
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        try {
            if (uploadAmount <= MAX_UPLOAD_AMOUNT) {
                String path = uploadFile(file.getBytes(), file.getSize(), extension);
                uploadAmount--;
                return FDFS_IMG_URL + path;
            } else {
                logger.info("启动缓存图片");
                return insert2Redis(file);
            }

        } catch (Exception e) {
            logger.info(e.toString());
            logger.info("文件上传失败，缓存入Redis处理，文件名",originalFileName);
            return insert2Redis(file);
        }
    }

    private String insert2Redis(MultipartFile file) {
        String contentType = file.getContentType();

        byte[] imageBytes = new byte[0];
        String key = null;
        try {
            imageBytes = file.getBytes();

            BASE64Encoder base64Encoder =new BASE64Encoder();

            String base64EncoderImg = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);

            base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");

            key = UUID.randomUUID().toString().replaceAll("-","");

            redisTemplate.opsForValue().set(key, base64EncoderImg);

            redisTemplate.opsForList().leftPush("IMGKEYS", key);

        } catch (IOException e) {
            logger.error("图片存入redis失败，图片丢失");
            e.printStackTrace();
        }
        return REDIS_IMG_URL + key;
    }
}
