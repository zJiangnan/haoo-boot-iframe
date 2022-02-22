package com.haoo.iframe.controller.user;

import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.request.DemoReq;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.util.FileUtils;
import com.haoo.iframe.util.RedisUtils;
import com.haoo.iframe.util.ReturnResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "测试控制器")
@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private DemoService demoService;

    @ApiOperation("查询plus")
    @PostMapping(value = "/users")
    public ReturnResponse usersList() {
        return demoService.findPage();
    }

    @ApiOperation("查询helper")
    @PostMapping(value = "/pageHelper")
    public ReturnResponse pageHelper() {
        return new ReturnResponse(200, "success", demoService.pageHelper());
    }

    @ApiOperation("添加")
    @PostMapping(value = "/addUsers")
    public ReturnResponse addUsers() {
        List<Demo> list = new ArrayList<>();
        Demo demo1 = new Demo();
        Demo demo = new Demo("wangerma", "man");
        demo.setTestName("testname");
        list.add(demo);
        return new ReturnResponse(200, "成功", demoService.saveBatch(list));
    }

}
