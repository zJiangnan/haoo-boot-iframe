package com.haoo.iframe.template.anotation;

import com.alibaba.fastjson.JSON;

public class Student {

    @AnotationField("name")
    private static String name;

    @AnotationField("age")
    private static Integer age;

    @AnotationField("sex")
    private static String sex;

    public Student(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Student.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        Student.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        Student.sex = sex;
    }
}
