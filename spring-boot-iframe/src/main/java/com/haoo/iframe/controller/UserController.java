package com.haoo.iframe.controller;

import com.github.pagehelper.PageInfo;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.util.RedisUtils;
import com.haoo.iframe.util.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "demo控制器")
@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private DemoService demoService;

    @ApiOperation("查询plus")
    @PostMapping(value = "/users")
    public ResultMessage usersList() {
        return demoService.findPage();
    }

    @ApiOperation("查询helper")
    @PostMapping(value = "/pageHelper")
    public ResultMessage pageHelper() {
        return new ResultMessage(200,"success",demoService.pageHelper());
    }

    @ApiOperation("添加")
    @PostMapping(value = "/addUsers")
    public ResultMessage addUsers() {
        List<Demo> list = new ArrayList<>();
        Demo demo1 = new Demo();
        Demo demo = new Demo("wangerma", "man");
        demo.setTestName("testname");
        list.add(demo);
        return new ResultMessage(200, "成功", demoService.saveBatch(list));
    }


}
