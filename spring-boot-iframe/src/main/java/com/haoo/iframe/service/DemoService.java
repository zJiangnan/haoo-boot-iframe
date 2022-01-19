package com.haoo.iframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.util.ResultMessage;

import java.util.List;

public interface DemoService extends IService<Demo> {

    ResultMessage findPage();

    PageInfo<Demo> pageHelper();

}
