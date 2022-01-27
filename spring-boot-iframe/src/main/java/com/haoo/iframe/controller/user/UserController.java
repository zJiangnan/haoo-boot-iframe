package com.haoo.iframe.controller.user;

import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.request.DemoReq;
import com.haoo.iframe.service.DemoService;
import com.haoo.iframe.util.FileUtil;
import com.haoo.iframe.util.RedisUtil;
import com.haoo.iframe.util.ReturnResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "测试控制器")
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    private RedisUtil redisUtil;

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
        return new ReturnResponse(200,"success",demoService.pageHelper());
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

    @ApiOperation("下载压缩包")
    @PostMapping(value = "/downloadZip")
    public void downloadZip(@RequestBody DemoReq req, HttpServletResponse response) {

        File[] srcFile = new File[req.getSrcPath().length];

        for (int i = 0; i < req.getSrcPath().length; i++) {
            File file = new File(req.getSrcPath()[i]);
            srcFile[i] = file;
        }
        //压缩后的文件目录
        String zipFilePath = req.getZipPath();
        File zipFile = new File(zipFilePath);
        //压缩文件 及 下载
        FileUtil.zipFiles(srcFile, zipFile);
        FileUtil.download(zipFilePath,response);
    }

    @ApiOperation("下载压缩包")
    @PostMapping(value = "/uploadFile/{pathParam}")
    public void uploadFile(@RequestBody DemoReq req,@PathVariable String pathParam) {

        File file = req.getFile();
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.length());

    }


}
