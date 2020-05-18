package com.kuaishou.kcode;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeMain {

    public static void main(String[] args) throws Exception {
        // "demo.data" 是你从网盘上下载的测试数据，这里直接填你的本地绝对路径
        InputStream fileInputStream = new FileInputStream("demo.data");
        KcodeQuestion question = new KcodeQuestion();
        // 准备数据
        question.prepare(fileInputStream);
        // 验证正确性
        String result = question.getResult(1L, "");
        if ("result".equals(result)) {
            System.out.println("success");
        } else {
            System.out.println("fail");
        }
    }
}