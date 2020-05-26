package com.kuaishou.kcode;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeMain {

    public static void main(String[] args) throws Exception {
        // "demo.data" 是你从网盘上下载的测试数据，这里直接填你的本地绝对路径
        InputStream fileInputStream = new FileInputStream("demo.data");
        Class<?> clazz = Class.forName("com.kuaishou.kcode.KcodeQuestion");
        Object instance = clazz.newInstance();
        Method prepareMethod = clazz.getMethod("prepare", InputStream.class);
        Method getResultMethod = clazz.getMethod("getResult", Long.class, String.class);
        // 准备数据
        prepareMethod.invoke(instance, fileInputStream);
        // 验证正确性
        Object result = getResultMethod.invoke(instance, new Long(1L), "methodName");
        if ("result".equals(result)) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}