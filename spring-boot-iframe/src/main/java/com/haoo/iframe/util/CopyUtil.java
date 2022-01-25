package com.haoo.iframe.util;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CopyUtil {

    public static class CopyClass implements Serializable {
        private String name;
        private String value;
        private CopyClassSon son;

        public CopyClass(String name, String value, CopyClassSon son) {
            this.name = name;
            this.value = value;
            this.son = son;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public CopyClassSon getSon() {
            return son;
        }

        public void setSon(CopyClassSon son) {
            this.son = son;
        }
    }

    public static class CopyClassSon implements Serializable {
        private String son;

        public CopyClassSon(String son) {
            this.son = son;
        }

        public String getSon() {
            return son;
        }

        public void setSon(String son) {
            this.son = son;
        }
    }



    public static class CopyClass1 implements Serializable {
        private String name;
        private String sex;
        private CopyClassSon1 son;

        public CopyClass1(String name, String sex, CopyClassSon1 son) {
            this.name = name;
            this.sex = sex;
            this.son = son;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public CopyClassSon1 getSon() {
            return son;
        }

        public void setSon(CopyClassSon1 son) {
            this.son = son;
        }
    }

    public static class CopyClassSon1 implements Serializable {
        private String son;

        public CopyClassSon1(String son) {
            this.son = son;
        }

        public String getSon() {
            return son;
        }

        public void setSon(String son) {
            this.son = son;
        }
    }



    public static void main(String[] args) {

        //testCopy();

        //defClsCopy();

        Long ss = 3232L;
        Long sss = 3232L;
        System.out.println(ss.equals(sss));
    }


    private static void defClsCopy(){
        CopyClassSon classSon = new CopyClassSon("son");
        CopyClass copyClass1 = new CopyClass("parent", "parent-value", classSon);
        CopyClass1 copyClass11 = copyProperties(copyClass1,CopyClass1.class);
        System.out.println(JSON.toJSON(copyClass11));
    }

    private static void testCopy(){
        CopyClassSon classSon = new CopyClassSon("son");
        CopyClass copyClass1 = new CopyClass("parent", "parent-value", classSon);
        CopyClass copyClass2 = copyProperties(copyClass1, CopyClass.class);
        classSon.setSon("更改了");

        System.out.println("原始对象：" + JSON.toJSON(copyClass1));
        System.out.println("复制对象：" + JSON.toJSON(copyClass2));


        List<CopyClass> list1 = new ArrayList<>();
        list1.add(copyClass1);
        list1.add(copyClass2);

        List<CopyClass> list2 = copyPropertiesArr(list1, CopyClass.class);
        for (CopyClass copyClass : list1) {
            copyClass.getSon().setSon("数组又改了一遍");
        }

        System.out.println("原始数组：" + JSON.toJSON(list1));
        System.out.println("复制数组：" + JSON.toJSON(list2));
    }

    private static <T> T copyProperties(Object obj, Class<T> cls) {
        String json = JSON.toJSONString(obj);
        return JSON.parseObject(json, (Type) cls);
    }

    private static <E> E copyPropertiesArr(Object obj, Class<?> cls) {
        String json = JSON.toJSONString(obj);
        return (E) JSON.parseArray(json, cls);
    }


}
