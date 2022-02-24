package com.haoo.iframe.controller.file;


import com.haoo.iframe.service.file.FileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "文件管理")
@RestController
@RequestMapping("/api/file/")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


}
