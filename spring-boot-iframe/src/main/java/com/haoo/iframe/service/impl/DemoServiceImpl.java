package com.haoo.iframe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.common.constant.UtilConstant;
import com.haoo.iframe.common.system.UploadParameter;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.mybatis.demo.DemoMapper;
import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public ReturnEntity findPage() {

        Integer current = 1;
        Integer size = 1;

        Page<Demo> userPage = new Page<>(current, size);
        Page<Demo> pageList = demoMapper.selectPage(userPage, new QueryWrapper<>());

        return new ReturnEntity(200, "success", pageList);
    }

    @Override
    public PageInfo<Demo> pageHelper() {
        Integer pageNum = 1;
        Integer pageSize = 1;
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Demo> list = new PageInfo<>(demoMapper.findPage());
        return list;
    }

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
        for (String file : files) {
            //暂停的文件标识
            UtilConstant.MAP.put(file, false);
        }
    }
}
