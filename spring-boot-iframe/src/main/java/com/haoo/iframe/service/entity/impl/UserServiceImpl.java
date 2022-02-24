package com.haoo.iframe.service.entity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haoo.iframe.entity.User;
import com.haoo.iframe.mybatis.mapper.UserMapper;
import com.haoo.iframe.service.entity.UserService;
import com.haoo.iframe.util.RedisUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final RedisUtils redisUtils;

    private final UserMapper userMapper;

    public UserServiceImpl(RedisUtils redisUtils, UserMapper userMapper) {
        this.redisUtils = redisUtils;
        this.userMapper = userMapper;
    }

}
