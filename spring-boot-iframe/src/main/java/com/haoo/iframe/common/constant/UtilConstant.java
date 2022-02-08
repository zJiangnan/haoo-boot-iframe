package com.haoo.iframe.common.constant;

import java.util.HashMap;
import java.util.Map;

public class UtilConstant {

    //储存断点续传标识
    public final static Map<String, Boolean> MAP = new HashMap<>();

    //每当进行写操作，同步的刷新到磁盘，刷新内容和元数据
    public final static String RANDOM_ACCESS_FILE_RWS = "rws";
    //每当进行写操作，同步的刷新到磁盘，刷新内容
    public final static String RANDOM_ACCESS_FILE_RWD = "rwd";

    //以只读的方式打开文本，也就意味着不能用write来操作文件
    public final static String RANDOM_ACCESS_FILE_R = "r";
    //读操作和写操作都是允许的
    public final static String RANDOM_ACCESS_FILE_RW = "rw";

    //上传文件拼接名称
    public final static String FILE_UTIL_SUFFIX_COPY = "_copy";


}
