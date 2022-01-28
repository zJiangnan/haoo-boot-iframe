package com.haoo.iframe.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "上传文件参数")
public class UploadReq implements Serializable {

    @ApiModelProperty(value = "上传路径", required = true)
    private String filePath;

    //断点续传指针
    private long pos;

}
