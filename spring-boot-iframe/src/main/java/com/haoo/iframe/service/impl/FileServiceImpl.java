package com.haoo.iframe.service.impl;

import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.FileService;
import com.haoo.iframe.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public void upload(UploadReq req, MultipartFile[] multipartFiles) {

        // 转换MultipartFile to File
        for (MultipartFile multipartFile : multipartFiles){
            //上传
            FileUtil.uploadFile(FileUtil.fileVMultipartFile(multipartFile),req.getFilePath());
        }

    }
}
