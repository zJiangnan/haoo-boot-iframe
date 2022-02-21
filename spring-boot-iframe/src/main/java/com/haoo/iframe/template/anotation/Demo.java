package com.haoo.iframe.template.anotation;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Demo {

    private static Student1 ss = new Student1("aaaa", 1, "男");

    public static void main(String[] args) throws Exception {


        /*List<Student> slist = new ArrayList<>();
        slist.add(new Student("a"));
        slist.add(new Student("b"));
        slist.add(new Student("c"));
        slist.add(new Student("d"));
        Optional<Student> stu = slist.stream().filter(f->f.getName().equals("d")).findAny();
        System.out.println(stu.get().getName());
        */

        /*Student student = new Student("aaa");
        Teacher teacher = new Teacher();
        teacher.setName("teacher");
        teacher.setStudent(student);

        Teacher teacher2 = (Teacher) SerializationUtils.clone(teacher);
        Teacher1 teacher1 = new Teacher1();
        BeanUtils.copyProperties(teacher,teacher1);
        student.setName("bbbb");
        teacher1.setName("teacher1");
        System.out.println(JSON.toJSON(teacher));
        System.out.println(JSON.toJSON(teacher1));
        System.out.println(JSON.toJSON(teacher2));*/

        /*List<Student> list = new ArrayList<>();
        list.add(new Student("a",1,"男"));
        list.add(new Student("a",2,"女"));
        list.add(new Student("c",3,"女"));
        list.add(new Student("d",4,"男"));
        list.add(new Student("e",5,"男"));

        Map<String, Student> s = list.stream().collect(Collectors.toMap(f-> f.getName() + f.getAge() , Function.identity()));

        String s1 = "a";
        String s2 = "a";

        System.out.println(s1.compareTo(s2));*/


        List list = new ArrayList<>();
        list.add(new Student("bbbb", 2, "女"));
        list.add(new Student1("aaaa", 1, "男"));
        //list.add(new TestAnotation());
        reflex(list);

    }

    public static void reflex(List<?> list) throws Exception {
        for (Object obj : list) {
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(AnotationField.class)) {
                    AnotationField anotationField = field.getAnnotation(AnotationField.class);
                    System.out.println(anotationField.value());
                    field.setAccessible(true);
                    System.out.println(field.get(cls));
                }
            }
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(AnotationMethod.class)) {
                    AnotationMethod anotationMethod = method.getAnnotation(AnotationMethod.class);
                    Class<?> anoClass = anotationMethod.value();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    method.getReturnType();
                    Object[] objs = new Object[]{};
                    for (Class clsp : parameterTypes) {
                        Object clsIns = clsp.newInstance();
                        //基本类型
                        if (clsIns.getClass().isPrimitive()) {

                        } else {
                            if (clsIns instanceof String) {
                                clsIns = "String 类型";
                            }
                            Constructor<?>[] constructors = anoClass.getDeclaredConstructors();
                            for (Constructor constructor : constructors) {

                            }
                        }

                        objs = ReflexUtils.addArr(objs, clsIns);
                    }

                    System.err.println("参数:" + JSON.toJSON(objs));

                    method.invoke(objs.getClass(), objs);

                }

            }
        }
    }


}
