package com.haoo.iframe.common.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    /***
     * demo 示例常量池
     */

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


    /**
     * 系统常量池
     */

    //初始化权限信息 redis:key
    public final static String SYS_INIT_PERMISSIONS = "sys_init_permissions";


    /**
     * 业务常量池
     */

    public final static String 业务常量 = "业务常量";


}
