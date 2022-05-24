package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirstTask {
    public static void main(String[] args) {
        System.out.println(" ====================  Hello world!   11111");


        Map<Integer, Integer> hashMap = new ConcurrentHashMap();//Collections.synchronizedMap(new HashMap<>());

        Thread thread1 = new Thread() {
            public void run() {
                for (int i = 1; i < 2147483000; i++) {
                    hashMap.put(i, i);
                    //System.out.println(" 1 ");
                }
            }
        };


        Thread thread2 = new Thread() {
            public void run() {
                int sum = 0;

                sum = hashMap.values().stream().mapToInt(key -> key).sum();

                System.out.println(" 2 =================" + sum);

            }
        };

        thread1.start();
        thread2.start();

    }
}
