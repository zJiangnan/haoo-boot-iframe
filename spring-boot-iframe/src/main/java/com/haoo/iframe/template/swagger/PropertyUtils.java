package com.haoo.iframe.template.swagger;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取properties配置文件工具类
 *
 * @author Logan
 *
 */
public class PropertyUtils {
    private static Properties property = new Properties();
    public static String[] rules;
    public static boolean swaggerEnabled;
    static {
        try (
                InputStream in = PropertyUtils.class.getResourceAsStream("/swagger-custom.properties");
        ) {
            property.load(in);
            rules = PropertyUtils.get("rules").split(",");
            swaggerEnabled = Boolean.parseBoolean(PropertyUtils.get("swaggerEnabled"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return property.getProperty(key);
    }

}
