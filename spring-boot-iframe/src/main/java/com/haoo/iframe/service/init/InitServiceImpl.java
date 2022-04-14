package com.haoo.iframe.service.init;

import com.haoo.iframe.common.constant.Constants;
import com.haoo.iframe.entity.Permissions;
import com.haoo.iframe.entity.User;
import com.haoo.iframe.service.entity.PermissionsService;
import com.haoo.iframe.service.entity.UserService;
import com.haoo.iframe.util.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitServiceImpl implements InitService {

    private final RedisUtils redisUtils;

    private final PermissionsService permissionsService;

    private final UserService userService;

    public InitServiceImpl(RedisUtils redisUtils, PermissionsService permissionsService, UserService userService) {
        this.redisUtils = redisUtils;
        this.permissionsService = permissionsService;
        this.userService = userService;
    }

    @PostConstruct
    public void initPermissions() {
        //初始化权限信息
        List<Permissions> list = permissionsService.list();
        if (!CollectionUtils.isEmpty(list)) {
            //权限列表
            List<String> authorities = list.stream().map(Permissions::getPermissionsName).collect(Collectors.toList());
            redisUtils.set(Constants.SYS_INIT_PERMISSIONS, authorities);
        }
    }

    @PostConstruct
    public void initAccount() {
        //初始化用户登录账户信息
        List<User> list = userService.list();
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(user -> redisUtils.set(user.getLoginName(), user));
        }
    }

}
