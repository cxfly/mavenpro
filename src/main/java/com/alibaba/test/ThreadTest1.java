package com.alibaba.test;

import java.util.ArrayList;
import java.util.List;

public class ThreadTest1 {
    public static void main(String[] args) throws InterruptedException {
        ThreadTest1 t = new ThreadTest1();
        t.doTest();
    }

    void doTest() throws InterruptedException {
        List<Thread> tlist = new ArrayList<Thread>();
        Counter counter = new Counter();
        Thread t1 = new Worker(counter, 0);
        Thread t2 = new Worker(counter, 1);
        Thread t3 = new Worker(counter, 1);
        tlist.add(t1);
        tlist.add(t2);
        tlist.add(t3);

        t1.start();
        t2.start();
        t3.start();

        while (!tlist.isEmpty()) {
            for (int i = 0; i < tlist.size(); i++) {
                Thread thread = tlist.get(i);
                if (!thread.isAlive()) {
                    tlist.remove(i);
                }
            }
            Thread.sleep(1);
        }
    }

    class Worker extends Thread {
        private final Counter counter;
        private final int     flag;

        public Worker(Counter counter, int flag) {
            this.counter = counter;
            this.flag = flag;
        }

        @Override
        public void run() {
            System.out.println("begin");
            if (flag == 0) {
                this.counter.increment();
            } else {
                this.counter.decrement();
            }
            System.out.println("end");
        }

    }

    class Counter {
        Object lock = new Object();

        public void increment() {
            System.out.println("begin increment");
            synchronized (lock) {
                lock.notifyAll();
                System.out.println("increment...");
            }
            System.out.println("end increment");
        }

        public void decrement() {
            System.out.println("begin decrement");
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                }
                System.out.println("decrement...");
            }
            System.out.println("end decrement");
        }
    }

}
