package com.cxfly.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class TestThreadNofity {
    static final int                                           NUM    = 10000 * 10;
    ConcurrentHashMap<Object, Object>                          doing  = new ConcurrentHashMap<Object, Object>();
    ConcurrentHashMap<Object, AtomicInteger>                   doing2 = new ConcurrentHashMap<Object, AtomicInteger>();
    ConcurrentHashMap<Object, Integer>                         doing4 = new ConcurrentHashMap<Object, Integer>();
    ConcurrentHashMap<Object, NotifyObj>                       doing5 = new ConcurrentHashMap<Object, NotifyObj>();
    ConcurrentHashMap<Object, AtomicStampedReference<Integer>> doing6 = new ConcurrentHashMap<Object, AtomicStampedReference<Integer>>();
    CountDownLatch                                             cdl    = new CountDownLatch(NUM);

    public static void main(String[] args) {
        TestThreadNofity t = new TestThreadNofity();
        t.doTest();
        //        System.out.println(t.doing.putIfAbsent("a", "1"));
        //        System.out.println(t.doing.putIfAbsent("a", "2"));
        //        System.out.println(t.doing.get("a"));
    }

    private void doTest() {
        List<String> tradeIds = this.generateData();
        ExecutorService threadPool = Executors.newFixedThreadPool(30);
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
            switch (type) {
                case 1:
                    test1();
                    break;
                case 2:
                    test2();
                    break;
                case 3:
                    test3();
                    break;
                case 4:
                    test4();
                    break;
                case 5:
                    test5();
                    break;
                case 6:
                    test6();
                    break;
                default:
                    test1();
            }
            cdl.countDown();
        }

        private void test1() {
            synchronized (doing) {
                while (doing.containsKey(tid)) {
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
            AtomicInteger val = doing2.get(tid);
            if (val == null) {
                synchronized (doing2) {
                    val = doing2.get(tid);
                    if (val == null) {
                        doing2.put(tid, new AtomicInteger());
                    }
                }
            }

            synchronized (val) {
                if (val.incrementAndGet() > 1) {
                    try {
                        val.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            this.processOrder(tid);

            synchronized (val) {
                if (val.decrementAndGet() > 0) {
                    val.notify();
                }
            }
        }

        private void test4() {
            synchronized (doing4) {
                Integer val = doing4.get(tid);
                if (val == null) {
                    doing4.put(tid, 1);
                } else {
                    doing4.put(tid, ++val);
                    if (doing4.get(tid) > 1) {
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
                    doing4.notify();
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
            val.decrementAndGet();
        }

        private void test5() {
            NotifyObj notifyObj = doing5.get(tid);
            if (notifyObj == null) {
                notifyObj = new NotifyObj();
                NotifyObj oldNotifyObj = doing5.putIfAbsent(tid, notifyObj);
                if (oldNotifyObj != null) {
                    notifyObj = oldNotifyObj;
                }
            }

            synchronized (notifyObj) {
                if (notifyObj.getCount() > 0) {
                    try {
                        notifyObj.setCount(notifyObj.getCount() + 1);
                        notifyObj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    notifyObj.setCount(1);
                }
            }

            this.processOrder(tid);

            synchronized (notifyObj) {
                notifyObj.setCount(notifyObj.getCount() - 1);
                if (notifyObj.getCount() > 0) {
                    notifyObj.notify();
                }
            }
        }

        private void test6() {
            AtomicStampedReference<Integer> val = null;
            if ((val = doing6.putIfAbsent(tid, new AtomicStampedReference<Integer>(0, 1))) == null) {
                val = doing6.get(tid);
            }

            for (;;) {
                int current = val.getReference();
                int next = current + 1;
                if (val.weakCompareAndSet(current, next, val.getStamp(), val.getStamp() + 1)) {
                    if (val.getReference() > 1) {
                        synchronized (val) {
                            try {
                                val.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }

            this.processOrder(tid);

            for (;;) {
                int current = val.getReference();
                int next = current - 1;
                if (val.weakCompareAndSet(current, next, val.getStamp(), val.getStamp() + 1)) {
                    if (val.getReference() > 0) {
                        synchronized (val) {
                            val.notify();
                        }
                    }
                    break;
                }
            }
        }

        private void processOrder(String tid2) {
            try {
                //                Thread.sleep(1);
                Thread.sleep((long) (Math.random() * 5));
                //                System.out.println(tid2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class NotifyObj {
        private int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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
