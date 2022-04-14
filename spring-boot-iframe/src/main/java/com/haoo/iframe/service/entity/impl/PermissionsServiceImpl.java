package com.haoo.iframe.service.entity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoo.iframe.entity.Permissions;
import com.haoo.iframe.mybatis.mapper.PermissionsMapper;
import com.haoo.iframe.service.entity.PermissionsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements PermissionsService {

    private final PermissionsMapper permissionsMapper;

    public PermissionsServiceImpl(PermissionsMapper permissionsMapper) {
        this.permissionsMapper = permissionsMapper;
    }

    @PostConstruct
    public void initPermissions(){

    }



}
