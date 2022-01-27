package com.haoo.iframe.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取yml配置文件工具类
 *
 * @author haoo
 */
@Slf4j
public class PropertiesUtil {
    private static Map<String, Map<String, Object>> ymlMap = new HashMap<>();

    private static Properties property = new Properties();
    public static String[] rules;
    public static boolean swaggerEnabled;

    static {
        try (
                InputStream in = PropertiesUtil.class.getResourceAsStream("/swagger-custom.properties");
        ) {
            property.load(in);
            rules = PropertiesUtil.getProperty("rules").split(",");
            swaggerEnabled = Boolean.parseBoolean(PropertiesUtil.getProperty("swaggerEnabled"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return property.getProperty(key);
    }

    static {
        Yaml yaml = new Yaml();
        try (
                InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("application-prod.yml");
        ) {
            ymlMap = yaml.loadAs(in, HashMap.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String getYaml(String key) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return ymlMap.get(key) == null ? null : String.valueOf(ymlMap.get(key));
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) ymlMap.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }

        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]) == null ? null : String.valueOf(finalValue.get(separatorKeys[separatorKeys.length - 1]));
    }

    public static void main(String[] args) {
        System.out.println(getProperty("swaggerEnabled"));
        System.out.println(getYaml("pagehelper.reasonable"));
    }

}