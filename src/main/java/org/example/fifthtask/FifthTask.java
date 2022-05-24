package org.example.fifthtask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

public class FifthTask {

    private static final String filepath="/Users/yuriikryvoruchko/Downloads/";

    static ServiceImpl service = new ServiceImpl();

    public static void main(String[] args) {
        System.out.println(" ====================  Hello world!   44444");


        FifthTask fifthTask = new FifthTask();

        User user1 = new User("User1");
        User user2 = new User("User2");
        User user3 = new User("User3");

        fifthTask.writeObjectToFile(user1, filepath+user1.getName());
        fifthTask.writeObjectToFile(user2, filepath+user2.getName());
        fifthTask.writeObjectToFile(user3, filepath+user3.getName());


        //Exchange
        User userOne = fifthTask.readObjectFromFile(filepath+user1.getName());
        User userTwo = fifthTask.readObjectFromFile(filepath+user2.getName());

        BigDecimal usd = service.exchangeHrnToUsd(userOne.getHrnAmount());
        BigDecimal hrn = service.exchangeHrnToUsd(userTwo.getUsdAmount());

        userOne.setHrnAmount(userOne.getHrnAmount().add(hrn));
        userOne.setUsdAmount(userOne.getUsdAmount().subtract(usd));
        userTwo.setUsdAmount(userTwo.getUsdAmount().add(usd));
        userTwo.setHrnAmount(userTwo.getHrnAmount().subtract(hrn));

        fifthTask.writeObjectToFile(userOne, filepath+userOne.getName());
        fifthTask.writeObjectToFile(userTwo, filepath+userTwo.getName());
        //






//        ExecutorService executor = Executors.newFixedThreadPool(4);
//        for (int i = 0; i < 20; i++) {
//            Runnable worker = new WorkerThread("" + i);
//            executor.execute(worker);
//        }
//        executor.shutdown();
//        while (!executor.isTerminated()) {  }
//        System.out.println("!!! Finished all threads !!!");

    }

    public void writeObjectToFile(User user, String path) {

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(user);
            objectOut.close();
            System.out.println("The " + user.getName() + "was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public User readObjectFromFile(String path) {
        User user = null;
        try {

            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            user = (User) ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

//    static class WorkerThread implements Runnable {
//        private String message;
//        public WorkerThread(String s){
//            this.message=s;
//        }
//        public void run() {
//            System.out.println(Thread.currentThread().getName()+" (Start) message = "+message);
//            processmessage();
//            System.out.println(Thread.currentThread().getName()+" (End)");
//        }
//        private void processmessage() {
//            try {  Thread.sleep(1000);  } catch (InterruptedException e) { e.printStackTrace(); }
//        }
//    }








}
