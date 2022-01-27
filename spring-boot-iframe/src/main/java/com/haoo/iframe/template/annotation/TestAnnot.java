package com.haoo.iframe.template.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试注解
 * <p></p>
 *
 * @author pluto
 * @version 1.0
 * @createdate 2022/1/27 11:32 AM
 * @see TestAnnot
 **/
@Pro(className = "com.haoo.iframe.template.annotation.Demo1",methodName = "show")
public class TestAnnot {

    public static void main(String[] args) {
//        1、获取该类的字节码文件对象
        Class<TestAnnot> testAnnotClass = TestAnnot.class;
//        2、获取上边的注解对象
        Pro annotations = testAnnotClass.getAnnotation(Pro.class);
//        3、调用注解对象中定义的抽象方法，获取返回值
        String className = annotations.className();
        String methodName = annotations.methodName();
        System.out.println(className);
        System.out.println(methodName);

        try {
//            4、加载该类进内存
            Class<?> cls = Class.forName(className);
//            5、创建对象
            Object obj = cls.getDeclaredConstructor().newInstance();
//            6、获取执行的方法
            Method method = cls.getMethod(methodName);
//            7、至此就可以调用Demo1里的show方法了
            method.invoke(obj);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
