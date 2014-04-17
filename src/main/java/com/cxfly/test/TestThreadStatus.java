package com.cxfly.test;

public class TestThreadStatus {
    
    static class Worker implements Runnable {

        @Override
        public void run() {
            String tName = Thread.currentThread().getName();
            System.out.println(tName+  " is executing!");
            Thread.dumpStack();
            synchronized (this) {
                if ("t1".equals(tName)) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    notify();
                }
                System.out.println(" write data ");
            }
            System.out.println(tName+ " exist!");
        }
    }
    
    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread t1 = new Thread(worker, "t1");
        Thread t2 = new Thread(worker, "t2");
        t1.start();   
        t2.start();   
        
        System.out.println("main thread exit.");
    }
}
