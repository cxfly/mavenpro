package com.cxfly.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TestSemaphore {
    static Semaphore s = new Semaphore(2, true);

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            threadPool.execute(new Worker(i));
        }

        threadPool.shutdown();

        try {
            Thread.currentThread().join(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Worker implements Runnable {
        int type;

        public Worker(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            try {
                s.acquire(1);
                System.out.println("doing...");

            } catch (InterruptedException e) {
            } finally {
                s.release(1);
            }

        }
    }
}
