package com.haoo.iframe.template.anotation;

import com.alibaba.fastjson.JSON;

public class TestAnotation {

    @AnotationMethod
    public static Student1 testMethod(Student stu,String str){
        System.out.println("返回："+JSON.toJSON(stu)+"==="+str);
        return null;
    }

    /*@AnotationMethod
    public static Student hahaMethod(String stu,String str){
        System.out.println("返回："+JSON.toJSON(stu)+"==="+str);
        return null;
    }*/
}
