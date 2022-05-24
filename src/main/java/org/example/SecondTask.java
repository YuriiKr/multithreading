package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondTask {
    public static void main(String[] args) {
        System.out.println(" ====================  Hello world!   22222");

        List<Integer> collection = new ArrayList<>();
        Random random = new Random();

        Thread thread1 = new Thread() {
            public void run() {
//                for (int a = 1; a < 2147483000; a++) {
//                    collection.add(a);
//                    System.out.println(" 1 ");
//                }

                while (true) {
                    synchronized (collection) {

                        collection.add(random.nextInt());
                    }
                    //System.out.println(" === " + res);
                }
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                long res = 0;
                while (true) {
                    synchronized (collection) {

                        res =  collection.stream().mapToInt(a -> a).sum();
                    }
                    System.out.println(" === " + res);
                }
            }
        };

        Thread thread3 = new Thread() {
            public void run() {
                long res = 0;
                for (int a = 1; a < 2147483000; a++) {

                     res = res + collection.get(a)*collection.get(a);
                    System.out.println(" ############## " + res*res);
                }
            }
        };

        thread1.start();
        thread2.start();
        //thread3.start();

    }
}
