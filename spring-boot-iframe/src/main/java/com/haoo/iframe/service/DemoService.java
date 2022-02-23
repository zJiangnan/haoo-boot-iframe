package com.haoo.iframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.request.UploadReq;
import org.springframework.web.multipart.MultipartFile;

public interface DemoService extends IService<Demo> {

    ReturnEntity findPage();

    PageInfo<Demo> pageHelper();

    void upload(UploadReq req, MultipartFile[] multipartFiles);

    void pause(String... files);

}
