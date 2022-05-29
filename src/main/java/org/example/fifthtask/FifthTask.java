package org.example.fifthtask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.fifthtask.enums.CurrencyEnum;
import org.example.fifthtask.enums.UsersEnum;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FifthTask {

    protected static final ReadWriteLock RW_LOCK = new ReentrantReadWriteLock();

    private static final Logger logger = LogManager.getLogger(FifthTask.class);

    private static final String filepath = "C:\\Users\\Yurii_Kryvoruchko\\Downloads\\multithreading\\multithreading";

    static ServiceImpl service = new ServiceImpl();

    private static final UsersEnum[] usersEnums = UsersEnum.values();
    private static final int usersEnumsSize = usersEnums.length;
    private static final Random RANDOM = new Random();

    public UsersEnum getRandomUser()  {
        return usersEnums[RANDOM.nextInt(usersEnumsSize)];
    }

    private static final CurrencyEnum[] currencyEnums = CurrencyEnum.values();
    private static final int currencyEnumsSize = usersEnums.length;

    public CurrencyEnum getRandomCurrencies()  {
        return currencyEnums[RANDOM.nextInt(currencyEnumsSize)];
    }

    public static void main(String[] args) {
        logger.info(" Fifth task started! ");

        FifthTask fifthTask = new FifthTask();

        User user1 = new User("User1");
        User user2 = new User("User2");
        User user3 = new User("User3");

        fifthTask.writeObjectToFile(user1, filepath + user1.getName());
        fifthTask.writeObjectToFile(user2, filepath + user2.getName());
        fifthTask.writeObjectToFile(user3, filepath + user3.getName());

        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 50; i++) {
            Runnable worker = new WorkerThread("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("!!! Finished all threads !!!");

    }

    static class WorkerThread implements Runnable {
        private String message;

        public WorkerThread(String s) {
            this.message = s;
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
                BigDecimal hrnAmount = BigDecimal.valueOf(Math.random()*1000);
                String userLeft = fifthTask.getRandomUser().toString();
                String userRight = fifthTask.getRandomUser().toString();
                while(userLeft.equals(userRight)) {
                    userRight = fifthTask.getRandomUser().toString();
                }
                String currencyLeft = fifthTask.getRandomCurrencies().toString();
                String currencyRight = fifthTask.getRandomCurrencies().toString();
                while(currencyLeft.equals(currencyRight)) {
                    currencyRight = fifthTask.getRandomCurrencies().toString();
                }
                fifthTask.exchange(userLeft, userRight, currencyLeft, hrnAmount, currencyRight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void exchange(String userOne, String userTwo, String user1Currency,
                         BigDecimal user1Amount, String user2Currency)
            throws IllegalArgumentException {
        FifthTask fifthTask = new FifthTask();
        User firstUser = fifthTask.readObjectFromFile(filepath + userOne);
        User secondUser = fifthTask.readObjectFromFile(filepath + userTwo);
        switch (user1Currency) {
            case "HRN":
                BigDecimal userOneHrnAmount = firstUser.getHrnAmount();
                logger.info("User " + firstUser.getName() + " has " + userOneHrnAmount.setScale(2, RoundingMode.HALF_EVEN)
                        + " " + user1Currency);
                if (userOneHrnAmount.compareTo(user1Amount) < 0) {
                    throw new IllegalArgumentException("User " + firstUser.getName() +
                            "doesn't have enough Hrn to exchange");
                }
                if (user2Currency.equals("HRN")) {
                    throw new IllegalArgumentException("User " + secondUser.getName() +
                            "doesn't have correct currency. Should be differ from Hrn");
                }
                if (user2Currency.equals("USD")) {
                    BigDecimal usd = service.exchangeHrnToUsd(user1Amount);
                    if (secondUser.getUsdAmount().compareTo(usd) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Usd to exchange");
                    }
                    firstUser.setHrnAmount(firstUser.getHrnAmount().subtract(user1Amount));
                    firstUser.setUsdAmount(firstUser.getUsdAmount().add(usd));
                    secondUser.setUsdAmount(secondUser.getUsdAmount().subtract(usd));
                    secondUser.setHrnAmount(secondUser.getHrnAmount().add(user1Amount));
                    logger.info("Exchange finished!");

                } else {
                    BigDecimal euro = service.exchangeHrnToEur(user1Amount);
                    if (secondUser.getEuroAmount().compareTo(euro) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Euro to exchange");
                    }
                    firstUser.setHrnAmount(firstUser.getHrnAmount().subtract(user1Amount));
                    firstUser.setEuroAmount(firstUser.getEuroAmount().add(euro));
                    secondUser.setEuroAmount(secondUser.getEuroAmount().subtract(euro));
                    secondUser.setHrnAmount(secondUser.getHrnAmount().add(user1Amount));
                    logger.info("Exchange finished!");
                }
                break;

            case "USD":
                BigDecimal userOneUsdAmount = firstUser.getUsdAmount();
                logger.info("User " + firstUser.getName() + " has " + userOneUsdAmount.setScale(2, RoundingMode.HALF_EVEN)
                        + " " + user1Currency);
                if (userOneUsdAmount.compareTo(user1Amount) < 0) {
                    throw new IllegalArgumentException("User " + firstUser.getName() +
                            "doesn't have enough Usd to exchange");
                }
                if (user2Currency.equals("USD")) {
                    throw new IllegalArgumentException("User " + secondUser.getName() +
                            "doesn't have correct currency. Should be differ from Usd");
                }
                if (user2Currency.equals("HRN")) {
                    BigDecimal hrn = service.exchangeUsdToHrn(user1Amount);
                    if (secondUser.getHrnAmount().compareTo(hrn) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Hrn to exchange");
                    }
                    firstUser.setHrnAmount(firstUser.getHrnAmount().subtract(user1Amount));
                    firstUser.setUsdAmount(firstUser.getUsdAmount().add(user1Amount));
                    secondUser.setUsdAmount(secondUser.getUsdAmount().subtract(user1Amount));
                    secondUser.setHrnAmount(secondUser.getHrnAmount().add(user1Amount));
                    logger.info("Exchange finished!");

                } else {
                    BigDecimal euro = service.exchangeHrnToEur(user1Amount);
                    if (secondUser.getEuroAmount().compareTo(euro) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Euro to exchange");
                    }
                    firstUser.setUsdAmount(firstUser.getUsdAmount().subtract(user1Amount));
                    firstUser.setEuroAmount(firstUser.getEuroAmount().add(euro));
                    secondUser.setEuroAmount(secondUser.getEuroAmount().subtract(euro));
                    secondUser.setUsdAmount(secondUser.getUsdAmount().add(user1Amount));
                    logger.info("Exchange finished!");
                }
                break;

            case "EURO":
                BigDecimal userOneEuroAmount = firstUser.getEuroAmount();
                logger.info("User " + firstUser.getName() + " has " + userOneEuroAmount.setScale(2, RoundingMode.HALF_EVEN)
                        + " " + user1Currency);
                if (userOneEuroAmount.compareTo(user1Amount) < 0) {
                    throw new IllegalArgumentException("User " + firstUser.getName() +
                            "doesn't have enough Euro to exchange");
                }
                if (user2Currency.equals("EURO")) {
                    throw new IllegalArgumentException("User " + secondUser.getName() +
                            "doesn't have correct currency. Should be differ from Euro");
                }
                if (user2Currency.equals("HRN")) {
                    BigDecimal hrn = service.exchangeUsdToHrn(user1Amount);
                    if (secondUser.getHrnAmount().compareTo(hrn) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Hrn to exchange");
                    }
                    firstUser.setEuroAmount(firstUser.getEuroAmount().subtract(user1Amount));
                    firstUser.setUsdAmount(firstUser.getUsdAmount().add(user1Amount));
                    secondUser.setUsdAmount(secondUser.getUsdAmount().subtract(user1Amount));
                    secondUser.setEuroAmount(secondUser.getEuroAmount().add(user1Amount));
                    logger.info("Exchange finished!");

                } else {
                    BigDecimal euro = service.exchangeHrnToEur(user1Amount);
                    if (secondUser.getEuroAmount().compareTo(euro) < 0) {
                        throw new IllegalArgumentException("User " + secondUser.getName() +
                                "doesn't have enough Usd to exchange");
                    }
                    firstUser.setUsdAmount(firstUser.getHrnAmount().subtract(user1Amount));
                    firstUser.setEuroAmount(firstUser.getEuroAmount().add(euro));
                    secondUser.setEuroAmount(secondUser.getEuroAmount().subtract(euro));
                    secondUser.setUsdAmount(secondUser.getHrnAmount().add(user1Amount));
                    logger.info("Exchange finished!");
                }
                break;

        }

        fifthTask.writeObjectToFile(firstUser, filepath + userOne);
        fifthTask.writeObjectToFile(secondUser, filepath + userTwo);
    }


    public void writeObjectToFile(User user, String path) {
        RW_LOCK.writeLock().lock();
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(user);
            objectOut.close();
            logger.info("The " + user.getName() + "was successfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RW_LOCK.writeLock().unlock();
        }
    }

    public User readObjectFromFile(String path) {
        User user = null;
        RW_LOCK.writeLock().lock();
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            user = (User) ois.readObject();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RW_LOCK.writeLock().unlock();
        }
        return user;
    }

}
