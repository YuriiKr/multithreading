package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FourthTask {

    public static void main(String[] args) {
        System.out.println(" ====================  Hello world!   44444");

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 20; i++) {
            Runnable worker = new WorkerThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {  }
        System.out.println("!!! Finished all threads !!!");

    }

    static class WorkerThread implements Runnable {
        private String message;
        public WorkerThread(String s){
            this.message=s;
        }
        public void run() {
            System.out.println(Thread.currentThread().getName()+" (Start) message = "+message);
            processmessage();
            System.out.println(Thread.currentThread().getName()+" (End)");
        }
        private void processmessage() {
            try {  Thread.sleep(1000);  } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

}
