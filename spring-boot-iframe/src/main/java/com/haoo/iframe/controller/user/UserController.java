package com.haoo.iframe.controller.user;

import com.haoo.iframe.common.system.ReturnEntity;
import com.haoo.iframe.service.UserService;
import com.haoo.iframe.util.RedisUtils;
import com.haoo.iframe.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试控制器")
@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final RedisUtils redisUtils;

    private final UserService userService;

    @Autowired
    public UserController(RedisUtils redisUtils, UserService userService) {
        this.redisUtils = redisUtils;
        this.userService = userService;
    }



    @ApiOperation("用户列表查询")
    @PostMapping(value = "/users")
    public ReturnEntity users(@RequestBody @Validated UserVo user) {
        return userService.findUsers(user);
    }


    @ApiOperation("添加用户")
    @PostMapping(value = "/addUser")
    public void addUser(@RequestBody @Validated UserVo user) {
        userService.addUser(user);
    }


}
