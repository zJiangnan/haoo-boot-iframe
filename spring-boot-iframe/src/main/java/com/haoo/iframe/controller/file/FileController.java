package com.haoo.iframe.controller.file;


import com.haoo.iframe.request.DemoReq;
import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.FileService;
import com.haoo.iframe.util.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Api(tags = "文件管理")
@RestController
@RequestMapping("/api/file/")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
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

        fileService.upload(req,multipartFiles);

    }

    @ApiOperation("暂停上传")
    @PostMapping(value = "/pause/{files}")
    public void pause(@PathVariable String... files) {

        fileService.pause(files);

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
}
