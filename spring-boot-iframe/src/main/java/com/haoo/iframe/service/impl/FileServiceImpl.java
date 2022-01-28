package com.haoo.iframe.service.impl;

import com.haoo.iframe.common.constant.UtilConstant;
import com.haoo.iframe.common.sysparameter.UploadParameter;
import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.FileService;
import com.haoo.iframe.util.BeanCopyUtils;
import com.haoo.iframe.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public void upload(UploadReq req, MultipartFile[] multipartFiles) {

        // 转换MultipartFile to File
        for (MultipartFile multipartFile : multipartFiles) {
            //文件参数
            UploadParameter param = new UploadParameter();
            param.setPos(req.getPos());
            param.setSavePath(req.getFilePath());
            //上传
            FileUtils.uploadFile(FileUtils.fileVMultipartFile(multipartFile), param);
        }

    }

    @Override
    public void pause(String... files) {
        for (String file : files){
            UtilConstant.MAP.put(file,false);
        }
    }
}
