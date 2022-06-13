package org.example.fifthtask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FifthTask {
    private static UserGenerator userGenerator = new UserGenerator();
    private static final Logger logger = LogManager.getLogger(FifthTask.class);
    private static ExchangeService exchangeService = new ExchangeService();
    private static final Random RANDOM = new Random();

    public User getRandomUser(List<User> storedUsers)  {
        int index = RANDOM.nextInt(storedUsers.size());
        return storedUsers.get(index);
    }

    public static void main(String[] args) throws InterruptedException {
        logger.info(" Fifth task started! ");
        List<User> storedUsers = userGenerator.generateUsers(20);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 50; i++) {
            Runnable worker = new WorkerThread(storedUsers);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
        System.out.println("!!! Finished all threads !!!");
    }
    static class WorkerThread implements Runnable {
        private final List<User> storedUsers;
        public WorkerThread(List<User> storedUsers) {
            this.storedUsers = storedUsers;
        }

        public void run() {
            logger.info(" (Start) Thread = "  + Thread.currentThread().getName());
            processExchange();
            logger.info(" (End) Thread = " + Thread.currentThread().getName());
        }

        private void processExchange() {
            try {
                Thread.sleep(1000);
                FifthTask fifthTask = new FifthTask();
                BigDecimal exchangeAmount = BigDecimal.valueOf(Math.random()*100);
                boolean userOneSell = RANDOM.nextBoolean();
                User userLeft = fifthTask.getRandomUser(storedUsers);
                User userRight = fifthTask.getRandomUser(storedUsers);
                while(userLeft.getName().equals(userRight.getName())
                        || userLeft.getCurrency().equals(userRight.getCurrency())) {
                    userRight = fifthTask.getRandomUser(storedUsers);
                }
                exchangeService.exchange(userLeft, userRight, exchangeAmount, userOneSell);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    }
