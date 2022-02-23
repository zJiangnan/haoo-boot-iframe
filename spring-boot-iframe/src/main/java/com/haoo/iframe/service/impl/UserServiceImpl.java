package com.haoo.iframe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haoo.iframe.common.enums.ApiCode;
import com.haoo.iframe.common.system.MD5;
import com.haoo.iframe.entity.User;
import com.haoo.iframe.mybatis.user.UserMapper;
import com.haoo.iframe.service.UserService;
import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.util.BeanCopyUtils;
import com.haoo.iframe.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ReturnEntity findUsers(UserVo userVo) {
        PageHelper.startPage(userVo.getPageNum(), userVo.getPageSize());
        PageInfo<User> list = new PageInfo<>(userMapper.selectList(new QueryWrapper<>()));
        return new ReturnEntity(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), list);
    }

    @Override
    public void addUser(UserVo userVo) {
        User user = BeanCopyUtils.copy(userVo,User.class);
        user.setPassword(MD5.getMD5String(userVo.getPassword()));
        userMapper.insert(user);
    }

}
