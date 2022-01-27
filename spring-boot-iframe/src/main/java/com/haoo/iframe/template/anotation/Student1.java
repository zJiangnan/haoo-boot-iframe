package com.haoo.iframe.template.anotation;

public class Student1 {

    @AnotationField(value = "name",index = "11")
    private static String name;

    @AnotationField("age")
    private static Integer age;

    @AnotationField("sex")
    private static String sex;

    public Student1(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Student1() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Student1.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        Student1.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        Student1.sex = sex;
    }
}
