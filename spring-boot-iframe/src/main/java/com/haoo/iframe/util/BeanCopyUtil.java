package com.haoo.iframe.utils;

import jodd.bean.BeanCopy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 拷贝 {@code bean} 对象工具类
 * <p>
 * 通过反射来实现 {@code bean} 对象的拷贝
 * </p>
 *
 * @author pluto
 * @version 1.0
 * @see jodd.bean.BeanCopy
 **/
public class BeanCopyUtil {

    /**
     * 单个对象进行 拷贝
     * <p>
     * 例如 DTO -> VO 对象
     * </p>
     *
     * @param m     源对象
     * @param clazz 目标对象class
     * @param <M>   源对象的泛型
     * @param <N>   目标对象的泛型
     * @return 返回拷贝后<b>目标对象</b>的 {@code bean Object}
     */
    public static <M, N> N copy(M m, Class<N> clazz) {
        try {
            N n = clazz.getDeclaredConstructor().newInstance();
            BeanCopy.beans(m, n).declared(true).copy();
            return n;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 集合对象进行 拷贝
     * <p>
     * 例如 DTO -> VO 对象
     * </p>
     *
     * @param mList     源对象集合
     * @param clazz 目标对象class
     * @param <M>   源对象的泛型
     * @param <N>   目标对象的泛型
     * @return  返回拷贝后目标对象 {@code N} 的集合
     */
    public static <M, N> List<N> copy(Collection<M> mList, Class<N> clazz) {
        try {
            ArrayList<N> nList = new ArrayList<>();
            for (M m : mList) {
                N n = clazz.getDeclaredConstructor().newInstance();
                BeanCopy.beans(m, n).declared(true).copy();
                nList.add(n);
            }
            return nList;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <M, N> N includeCopy(M m, Class<N> clazz, String... fields) {
        try {
            N n = clazz.getDeclaredConstructor().newInstance();
            ((BeanCopy) BeanCopy.beans(m, n).include(fields)).declared(true).copy();
            return n;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <M, N> List<N> includeCopy(Collection<M> mList, Class<N> clazz, String... fields) {
        try {
            ArrayList<N> nList = new ArrayList<>();
            for (M m : mList) {
                N n = clazz.getDeclaredConstructor().newInstance();
                ((BeanCopy) BeanCopy.beans(m, n).include(fields)).declared(true).copy();
                nList.add(n);
            }
            return nList;
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <M, N> N excludeCopy(M m, Class<N> clazz, String... fields) {
        try {
            N n = clazz.getDeclaredConstructor().newInstance();
            ((BeanCopy) BeanCopy.beans(m, n).exclude(fields)).declared(true).copy();
            return n;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <M, N> List<N> excludeCopy(Collection<M> mList, Class<N> clazz, String... fields) {
        try {
            ArrayList<N> nList = new ArrayList<>();
            for (M m : mList) {
                N n = clazz.getDeclaredConstructor().newInstance();
                ((BeanCopy) BeanCopy.beans(m, n).exclude(fields)).declared(true).copy();
                nList.add(n);
            }
            return nList;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}