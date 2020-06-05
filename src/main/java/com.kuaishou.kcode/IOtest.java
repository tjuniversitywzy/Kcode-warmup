package com.kuaishou.kcode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class IOtest {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("/home/wzy/warm/warmup-test.data");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while (bufferedReader.read() != -1){
                System.out.println(bufferedReader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }
}
