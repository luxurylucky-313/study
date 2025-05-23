package com.zyt.xxt.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zyt.xxt.service.OssService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class OssServiceImpl implements OssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    private OSS ossClient;

    // 使用@PostConstruct确保在依赖注入完成后初始化ossClient
    @PostConstruct
    public void initOSSClient() {
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public void uploadFile(String fileName, InputStream fileContent) {
        try {
            if (ossClient != null) {
                // 创建文件上传请求
                ossClient.putObject(bucketName, fileName, fileContent);
            } else {
                throw new IllegalStateException("OSS Client has not been initialized.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        // 生成文件的 URL
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }

    @Override
    public void close() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }
}