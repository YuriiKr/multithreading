package org.example;

import java.util.LinkedList;
import java.util.Queue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ThirdTask {

    public static void main(String[] args) {
        System.out.println(" ====================  Hello world!   33333");

        DataQueue dataQueue = new DataQueue(20);

        Producer producer = new Producer(dataQueue);
        Thread producerThread = new Thread(producer);

        Consumer consumer = new Consumer(dataQueue);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        long b = 0;
        for (int i = 0; i<2147483640; i++) {
            b = b+i;
        }

        producer.stop();
        consumer.stop();

    }
    @Data
    public static class DataQueue {
        private final Queue<Message> queue = new LinkedList<>();
        private final int maxSize;
        private final Object FULL_QUEUE = new Object();
        private final Object EMPTY_QUEUE = new Object();

        DataQueue(int maxSize) {
            this.maxSize = maxSize;
        }

        public void waitOnFull() throws InterruptedException {
            synchronized (FULL_QUEUE) {
                FULL_QUEUE.wait();
            }
        }

        public void notifyAllForFull() {
            synchronized (FULL_QUEUE) {
                FULL_QUEUE.notifyAll();
            }
        }

        public void waitOnEmpty() throws InterruptedException {
            synchronized (EMPTY_QUEUE) {
                EMPTY_QUEUE.wait();
            }
        }

        public void notifyAllForEmpty() {
            synchronized (EMPTY_QUEUE) {
                EMPTY_QUEUE.notify();
            }
        }

        public void add(Message message) {
            synchronized (queue) {
                queue.add(message);
            }
        }

        public Message remove() {
            synchronized (queue) {
                return queue.poll();
            }
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private int id;
        private double data;

    }



    public static class Producer implements Runnable {
        private final DataQueue dataQueue;
        private volatile boolean runFlag;

        public Producer(DataQueue dataQueue) {
            this.dataQueue = dataQueue;
            runFlag = true;
        }

        @Override
        public void run() {
            produce();
        }

        public void produce() {
            while (runFlag) {
                Message message = generateMessage();
                while (isFull(dataQueue)) {
                    try {
                        dataQueue.waitOnFull();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                if (!runFlag) {
                    break;
                }

                dataQueue.add(message);
                dataQueue.notifyAllForEmpty();
            }
        }

        public Message generateMessage() {
            return new Message((int) (Math.random()*10000), Math.random());
        }

        public boolean isFull (DataQueue dataQueue) {
            return dataQueue.getMaxSize() == dataQueue.getQueue().size();
        }

        public void stop() {
            runFlag = false;
            dataQueue.notifyAllForFull();
        }

    }


    public static class Consumer implements Runnable {
        private final DataQueue dataQueue;
        private volatile boolean runFlag;

        public Consumer(ThirdTask.DataQueue dataQueue) {
            this.dataQueue = dataQueue;
            runFlag = true;
        }

        @Override
        public void run() {
            consume();
        }

        public void consume() {
            while (runFlag) {
                Message message;
                if (isEmpty(dataQueue)) {
                    try {
                        dataQueue.waitOnEmpty();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                if (!runFlag) {
                    break;
                }
                message = dataQueue.remove();
                System.out.println("message = " + message.toString());
                dataQueue.notifyAllForFull();
                //useMessage(message);

            }
        }

        public boolean isEmpty (DataQueue dataQueue) {
            return dataQueue.getQueue().size() == 0;
        }

        public void stop() {
            runFlag = false;
            dataQueue.notifyAllForEmpty();
        }

    }

}
