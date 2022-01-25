package com.haoo.iframe.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties配置文件工具类
 *
 * @author Logan
 *
 */
public class PropertyUtil {
    private static Properties property = new Properties();
    public static String[] rules;
    public static boolean swaggerEnabled;
    static {
        try (
                InputStream in = PropertyUtil.class.getResourceAsStream("/swagger-custom.properties");
        ) {
            property.load(in);
            rules = PropertyUtil.get("rules").split(",");
            swaggerEnabled = Boolean.parseBoolean(PropertyUtil.get("swaggerEnabled"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return property.getProperty(key);
    }

}
