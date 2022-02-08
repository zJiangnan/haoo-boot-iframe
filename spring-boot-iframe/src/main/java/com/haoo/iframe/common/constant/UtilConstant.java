package com.haoo.iframe.common.constant;

import java.util.HashMap;
import java.util.Map;

public interface UtilConstant {

    //储存断点续传标识
    Map<String, Boolean> MAP = new HashMap<>();

    //每当进行写操作，同步的刷新到磁盘，刷新内容和元数据
    String RANDOM_ACCESS_FILE_RWS = "rws";
    //每当进行写操作，同步的刷新到磁盘，刷新内容
    String RANDOM_ACCESS_FILE_RWD = "rwd";

    //以只读的方式打开文本，也就意味着不能用write来操作文件
    String RANDOM_ACCESS_FILE_R = "r";
    //读操作和写操作都是允许的
    String RANDOM_ACCESS_FILE_RW = "rw";

    //上传文件拼接名称
    String FILE_UTIL_SUFFIX_COPY = "_copy";


}
