package com.haoo.iframe.template.anotation;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflexUtils {

    private static boolean eqPackage = true;
    private static String packageUrl = "com.haoo.iframe";


    public static void invokeMethod(String methodName, Class<?> cls) {

    }

    public static List<String> getMethod(Method... methods) {
        print("###Method###:");
        System.out.println(JSON.toJSON(Arrays.stream(methods).map(Method::getName).collect(Collectors.toList())));
        for (Method method : methods) {
            print("###" + method.getName() + "--Param###:");
            System.out.println(JSON.toJSON(getClassTypes(method.getParameterTypes())));
            print("###" + method.getName() + "--ReValue###:");
            System.out.println(JSON.toJSON(getClassTypes(method.getReturnType())));
            System.out.println();
        }
        return Arrays.stream(methods).map(Method::getName).collect(Collectors.toList());
    }

    public static void getField(Field... fields) {
        System.out.print("###Field###");
        System.out.println(Arrays.stream(fields).map(Field::getName).collect(Collectors.toList()));
        for (Field field : fields) {
            //System.out.println(field.getName());
        }
    }

    public static Object[] getClassTypes(Class<?>... parameterTypes) {
        Object[] objs = new Object[]{};
        for (Class clsIns : parameterTypes) {
            objs = addArr(objs, clsIns);

            if (checkInstance(clsIns)) {
                if (clsIns.getPackage().getName().startsWith(packageUrl)) {
                    getClassInfo(clsIns);
                }
            }
        }
        return objs;
    }

    public static boolean checkInstance(Class cls) {
        boolean flag = true;
        Object obj = null;
        try {
            /*if ("void".equals(cls.getName())){
                return false;
            }
            if ("java.lang.Integer".equals(cls.getName())){
                return false;
            }*/
            obj = cls.newInstance();
        } catch (Exception e) {
            return false;
        }

        if (obj.getClass().isPrimitive()) {
            //基本类型
            flag = false;
        } else if (obj instanceof Integer) {
            flag = false;
        } else if (obj instanceof String) {
            flag = false;
        } else if (obj instanceof String) {
            flag = false;
        }
        return flag;
    }

    public static Object[] addArr(Object[] arr, Object obj) {
        Object[] res = new Object[arr.length + 1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            res[index] = arr[i];
            index++;
        }
        res[res.length - 1] = obj;
        return res;
    }


    public static void getClassInfo(Class<?> cls) {
        print("###Class###:");
        System.out.println(cls.getName());
        getField(cls.getDeclaredFields());
        getMethod(cls.getDeclaredMethods());
    }


    private static void print(Object obj){
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
        System.out.print("\033[1;" + color + "m" + obj + "\033[0m");
    }



    public static void main(String[] args) {
        TestAnotation testAnotation = new TestAnotation();
        getClassInfo(testAnotation.getClass());

    }


}
