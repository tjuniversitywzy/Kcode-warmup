package com.kuaishou.kcode;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

/**
 * @author kcode
 * Created on 2020-05-20
 */
public class KcodeQuestion {
    HashMap<String, HashMap<String,String>> res = new HashMap<>(128);
    /**
     * prepare() 方法用来接受输入数据集，数据集格式参考README.md
     *
     * @param inputStream
     */
    public void prepare(InputStream inputStream) {
        //map方法名称-map时间-评价指标
        Stack<String> timeStack = new Stack<>();
        ArrayDeque<String> stack = new ArrayDeque<>();//装全部的字段
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader= new BufferedReader(inputStreamReader);
            while (bufferedReader.read() != -1){
                String s = bufferedReader.readLine();
                //判断s的前10个字符串是否和之前的相同
                if (timeStack.isEmpty()){
                    timeStack.push(s.substring(0,9));
                    stack.push(s);
                    continue;
                }
                if (timeStack.peek().equals(s.substring(0,9))){
                    //同一秒内
                    stack.push(s);
                }else {
                    //不是同一秒,要开始计算上一秒的内容,同时要把当次读到的字符串放入timestack和stack里面
                    //QPS和其他的不一样，可以直接通过stack的大小获取
                    calPreSecond(stack,timeStack);
                    timeStack.pop();
                    timeStack.push(s.substring(0,9));
                    stack.push(s);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //用于计算上一秒的结果
    //将上一秒的结果封装到map
    public void calPreSecond(ArrayDeque<String> stack,Stack<String> timeStack){
        HashMap<String, PriorityQueue<String[]>> map = new HashMap<>();
        while (!stack.isEmpty()){//执行建堆过程
            //弹出每一个字符串进行处理
            String pop = stack.pop();
            String[] split = pop.split(",");
            if (map.containsKey(split[1])){
                PriorityQueue<String[]> priorityQueue = map.get(split[1]);//获取该队列
                priorityQueue.offer(new String[]{split[0],split[2]});
            }else {
                PriorityQueue<String[]> priorityQueue = new PriorityQueue<>(new Comparator<String[]>() {
                    @Override
                    public int compare(String[] o1, String[] o2) {
                        return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]);//大顶堆
                    }
                });
                priorityQueue.offer(new String[]{split[0],split[2]});
                map.put(split[1],priorityQueue);
            }
        }
        Set<String> strings = map.keySet();
        for (String str : strings){
            PriorityQueue<String[]> priorityQueue = map.get(str);//当前方法名称的队列，对其进行计算
            int size = priorityQueue.size();
            double sum = 0;
            int index = size;//用于计算P系列指标
            int p99 = (int)Math.ceil(size * 0.99);
            int p50 = (int)Math.ceil(size * 0.5);
            String p99Result = "";
            String p50Result = "";
            String MAX = "";
            String AVG = "";
            int qps = size;
            while (!priorityQueue.isEmpty()){
                String s = priorityQueue.poll()[1];
                if (index == size) MAX = s;
                if (index == p99) p99Result = s;
                if (index == p50) p50Result = s;
                sum = sum + Long.parseLong(s);
                index--;
            }
            AVG = String.valueOf((int) Math.ceil(sum/size));
            HashMap<String, String> stringStringHashMap = null;
            if (!res.containsKey(str)){
                stringStringHashMap = new HashMap<String, String>(4200);
                res.put(str,stringStringHashMap);
            }else {
                stringStringHashMap = res.get(str);
            }
            stringStringHashMap.put(timeStack.peek(),qps+","+p99Result.concat(",").concat(p50Result).concat(",").concat(AVG).concat(",").concat(MAX));
            res.put(str,stringStringHashMap);
        }
    }


    /**
     * getResult() 方法是由kcode评测系统调用，是评测程序正确性的一部分，请按照题目要求返回正确数据
     * 输入格式和输出格式参考 README.md
     *
     * @param timestamp 秒级时间戳
     * @param methodName 方法名称
     */
    public String getResult(Long timestamp, String methodName) {
        // do something
        return res.get(methodName).get(String.valueOf(timestamp).substring(1));
    }
}
