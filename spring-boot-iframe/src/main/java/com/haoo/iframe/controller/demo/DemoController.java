package com.haoo.iframe.controller.demo;

import com.alibaba.fastjson.JSON;
import com.haoo.iframe.entity.Demo;
import com.haoo.iframe.request.DemoReq;
import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.demo.DemoService;
import com.haoo.iframe.util.BeanCopyUtils;
import com.haoo.iframe.util.ExcelUtils;
import com.haoo.iframe.util.FileUtils;
import com.haoo.iframe.util.RestTemplateUtils;
import com.haoo.iframe.vo.ExcelVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 测试接口
 * <p></p>
 *
 * @author pluto
 * @version 1.0
 * @see DemoController
 **/
@RestController
@RequestMapping("/t")
@Api(tags = "测试接口")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @ApiOperation("测试")
    @GetMapping("/test")
    public String test() {
        try {
            ResponseEntity<String> post = RestTemplateUtils.post("http://echo:8080/t/test1", "", new HttpHeaders());
            System.out.println(post);
            System.out.println(post.getBody());
            return "完成";
        } catch (Exception e) {
            e.printStackTrace();
            return "失败";
        }
    }

    @ApiOperation("测试1")
    @PostMapping("/test1")
    public String test1() {
        return "你好！";
    }


    @ApiOperation("上传文件")
    @PostMapping(value = "/upload")
    public void upload(@RequestParam("file") MultipartFile[] multipartFiles, UploadReq req) {

        /***
         * MultipartFile Content-Type:multipart/form-data
         * 后端收到请求时由于请求体有 @RequestBody 注解标识，通常会用application/json, application/xml处理content-type
         * MultipartFile这样的multipart/form-data媒体类型优先级会高于application/json，
         * 而配置@RequestBody 感觉使用一个低优先级的handle一个高优先级的从而报错
         * 所以在multipart/form-data媒体类型请求时将 @RequestBody去掉就可以了。
         */

        demoService.upload(req,multipartFiles);

    }

    @ApiOperation("暂停上传")
    @PostMapping(value = "/pause/{files}")
    public void pause(@PathVariable String... files) {

        demoService.pause(files);

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
        FileUtils.zipFiles(srcFile, zipFile);
        FileUtils.download(zipFilePath, response);
    }


    @ApiOperation("下载报表")
    @GetMapping(value = "/downloadReport")
    public void downloadReport(HttpServletResponse response) {

        List<ExcelVO> list = JSON.parseArray("[{'name':'v1-name','sex':'v1-sex'},{'name':'v2-name','sex':'v2-sex'},{'name':'v3-name','sex':'v3-sex'}]"
                , ExcelVO.class);
        ExcelUtils.writeExcel(response, list, ExcelVO.class, "生成的报表.xlsx");
    }

    @ApiOperation("保存报表")
    @PostMapping(value = "/saveExcel")
    public void saveExcel(@RequestParam("file") MultipartFile multipartFiles) {
        List<ExcelVO> list = ExcelUtils.readExcel(ExcelVO.class, multipartFiles);
        List<Demo> saveList = BeanCopyUtils.copy(list, Demo.class);
        demoService.saveBatch(saveList);
    }

}
