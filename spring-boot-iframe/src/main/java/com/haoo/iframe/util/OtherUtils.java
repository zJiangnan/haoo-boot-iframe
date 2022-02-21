package com.haoo.iframe.util;

import org.apache.commons.lang.StringUtils;

import java.util.Random;

public class OtherUtils {

    public static void println(Object obj) {
        /*
        颜色代码: QUOTE:
        字背景颜色范围: 40--49          字颜色: 30—39
        ANSI控制码:
        QUOTE:
           \033[0m   关闭所有属性
           \033[1m   设置高亮度
           \033[4m   下划线
           \033[5m   闪烁
           \033[7m   反显
           \033[8m   消隐
           \033[30m   --   \033[37m   设置前景色
           \033[40m   --   \033[47m   设置背景色
           \033[nA   光标上移n行
           \03[nB   光标下移n行
           \033[nC   光标右移n行
           \033[nD   光标左移n行
        */
        Random rand = new Random();
        int max = 37;
        int min = 30;
        int color = rand.nextInt(max - min + 1) + min;
        System.out.println("\033[1;" + color + "m" + obj + "\033[0m");
    }

    /**
     * 随机key
     *
     * @return
     */
    public static String random(String code) {
        int random = (int) (Math.random() * 1000);
        String key = System.currentTimeMillis() + random + "";
        return StringUtils.isEmpty(code) ? key : code + key;
    }

    /**
     * 随机key
     *
     * @return
     */
    public static String random() {
        int random = (int) (Math.random() * 1000);
        String key = System.currentTimeMillis() + random + "";
        return key;
    }

}
