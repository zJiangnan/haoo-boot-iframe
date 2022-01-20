package com.haoo.iframe.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.util.ReturnResponse;

public interface DemoService extends IService<Demo> {

    ReturnResponse findPage();

    PageInfo<Demo> pageHelper();

}
