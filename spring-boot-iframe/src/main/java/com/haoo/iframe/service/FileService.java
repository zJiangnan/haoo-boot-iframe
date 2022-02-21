package com.haoo.iframe.service;

import com.haoo.iframe.request.UploadReq;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void upload(UploadReq req, MultipartFile[] multipartFiles);

    void pause(String... files);
    
}
