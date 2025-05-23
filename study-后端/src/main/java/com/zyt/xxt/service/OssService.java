package com.zyt.xxt.service;

import java.io.InputStream;

public interface OssService {
    void uploadFile(String fileName, InputStream fileContent);
    String getFileUrl(String fileName);
    void close();
}