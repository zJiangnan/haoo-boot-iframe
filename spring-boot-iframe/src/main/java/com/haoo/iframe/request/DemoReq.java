package com.haoo.iframe.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;

@Data
@ApiModel(value = "测试传参类")
public class DemoReq implements Serializable {

    @ApiModelProperty(value = "要压缩文件的路径数组", required = true)
    private String[] srcPath;

    @ApiModelProperty(value = "压缩后文件储存路径", required = true)
    private String zipPath;

}
