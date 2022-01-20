package com.haoo.iframe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.mybatis.demo.DemoMapper;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.util.ReturnResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public ReturnResponse findPage() {

        Integer current = 1;
        Integer size = 1;

        Page<Demo> userPage = new Page<>(current, size);
        Page<Demo> pageList = demoMapper.selectPage(userPage, new QueryWrapper<>());

        return new ReturnResponse(200, "success", pageList);
    }

    @Override
    public PageInfo<Demo> pageHelper() {
        Integer pageNum = 1;
        Integer pageSize = 1;
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Demo> list = new PageInfo<>(demoMapper.findPage());
        return list;
    }
}
