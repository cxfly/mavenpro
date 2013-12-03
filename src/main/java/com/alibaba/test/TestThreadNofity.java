package com.alibaba.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadNofity {
    static final int                         NUM    = 10000 * 10;
    ConcurrentHashMap<Object, Object>        doing  = new ConcurrentHashMap<Object, Object>();
    ConcurrentHashMap<Object, AtomicInteger> doing2 = new ConcurrentHashMap<Object, AtomicInteger>();
    ConcurrentHashMap<Object, Integer>       doing4 = new ConcurrentHashMap<Object, Integer>();
    CountDownLatch                           cdl    = new CountDownLatch(NUM);

    public static void main(String[] args) {
        TestThreadNofity t = new TestThreadNofity();

        t.doTest();
    }

    private void doTest() {
        List<String> tradeIds = this.generateData();
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();
        for (String tid : tradeIds) {
            threadPool.execute(new Worker(tid, 1));
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
        }

        long end = System.currentTimeMillis();

        System.out.println("--->" + (end - start));

        threadPool.shutdown();
    }

    class Worker implements Runnable {
        String tid;
        int    type;

        public Worker(String tid, int type) {
            this.tid = tid;
            this.type = type;
        }

        @Override
        public void run() {
            if (type == 1) {
                test1();
            } else if (type == 2) {
                test2();
            } else if (type == 3) {
                test3();
            } else {
                test4();
            }
            cdl.countDown();
        }

        private void test1() {
            synchronized (doing) {
                while (doing.contains(tid)) {
                    try {
                        doing.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                doing.put(tid, "");
            }

            this.processOrder(tid);

            synchronized (doing) {
                doing.remove(tid);
                doing.notifyAll();
            }
        }

        private void test2() {
            AtomicInteger val = null;
            synchronized (doing2) {
                val = doing2.get(tid);
                if (val == null) {
                    val = new AtomicInteger();
                    doing2.put(tid, val);
                }
                if (val.incrementAndGet() > 1) {
                    try {
                        doing2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.processOrder(tid);

            if (val.decrementAndGet() > 0) {
                synchronized (doing2) {
                    doing2.notifyAll();
                }
            }
        }

        private void test4() {
            synchronized (doing4) {
                Integer val = doing4.get(tid);
                if (val == null) {
                    doing4.put(tid, 1);
                } else {
                    doing4.put(tid, val++);
                    while (doing4.get(tid) > 1) {
                        try {
                            doing4.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            this.processOrder(tid);

            synchronized (doing4) {
                int val2 = doing4.get(tid) - 1;
                doing4.put(tid, val2);
                if (val2 > 0) {
                    doing4.notifyAll();
                }
            }
        }

        private void test3() {
            AtomicInteger val = null;

            synchronized (doing2) {
                val = doing2.get(tid);
                if (val == null) {
                    val = new AtomicInteger();
                    doing2.put(tid, val);
                }
            }

            while (!val.compareAndSet(0, 1)) {
                try {
                    Thread.sleep(0, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.processOrder(tid);

            if (val.decrementAndGet() == 0) {
                doing2.remove(val);
            }
        }

        private void processOrder(String tid2) {
            try {
                //                Thread.sleep(1);
                Thread.sleep((long) (Math.random() * 10));
                //                System.out.println(tid2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> generateData() {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < NUM; i++) {
            result.add("100" + (int) (Math.random() * 100));
            //            result.add(String.valueOf(i));
            //            result.add("100");
            //            result.add("200");
            //            result.add("300");
        }
        return result;
    }

}
