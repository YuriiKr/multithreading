package org.example.fifthtask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserRepository {
    private static final String path = "src/main/resources";
    private static final Logger logger = LogManager.getLogger(FifthTask.class);
    protected static final ReadWriteLock RW_LOCK = new ReentrantReadWriteLock();

    public static void writeUserToFile(User user) {
        RW_LOCK.writeLock().lock();
        try (FileOutputStream fileOut = new FileOutputStream(path+user.getName());
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(user);
            logger.info("The " + user.getName() + "was successfully written to a file");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RW_LOCK.writeLock().unlock();
        }
    }

    public static User readUserFromFile(User user) {
        RW_LOCK.readLock().lock();
        try (FileInputStream fis = new FileInputStream(path+user.getName());
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            user = (User) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RW_LOCK.readLock().unlock();
        }
        return user;
    }
}
