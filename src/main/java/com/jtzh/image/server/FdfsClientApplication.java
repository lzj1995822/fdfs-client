package com.jtzh.image.server;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

@Import(FdfsClientConfig.class)

@SpringBootApplication
public class FdfsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FdfsClientApplication.class, args);
    }

}
