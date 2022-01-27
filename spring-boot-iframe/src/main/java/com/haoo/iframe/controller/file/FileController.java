package com.haoo.iframe.controller.file;


import com.haoo.iframe.request.UploadReq;
import com.haoo.iframe.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "测试控制器")
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
}
