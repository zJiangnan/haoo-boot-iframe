package com.haoo.iframe.common.system;

import lombok.Data;

@Data
public class UploadParameter {

    //保存路径 d:/test/test.txt
    private String savePath;

    //断点续传指针
    private long pos;
}
