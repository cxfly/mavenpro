package com.alibaba.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadNofity1 {
    static final int                         NUM     = 10000 * 10;
    ConcurrentHashMap<Object, Object>        doing   = new ConcurrentHashMap<Object, Object>();
    ConcurrentHashMap<Object, AtomicInteger> doing2  = new ConcurrentHashMap<Object, AtomicInteger>();
    ConcurrentHashMap<Object, Integer>       doing4  = new ConcurrentHashMap<Object, Integer>();
    CountDownLatch                           cdl     = new CountDownLatch(NUM);
    ConcurrentHashMap<Object, NotifyObj>     doing5  = new ConcurrentHashMap<Object, NotifyObj>();
    HashMap<String, Data>                    dataMap = new HashMap<String, Data>();

    public static void main(String[] args) {
        TestThreadNofity1 t = new TestThreadNofity1();
        t.doTest();
    }

    private void doTest() {
        List<String> tradeIds = this.generateData();
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        long start = System.currentTimeMillis();

        for (String tid : tradeIds) {
            Data data = dataMap.get(tid);
            if (data == null) {
                data = new Data(tid, false);
            } else {
                data.setRepeat(true);
            }
            dataMap.put(tid, data);
        }

        for (String tid : tradeIds) {
            threadPool.execute(new Worker(tid, 5));
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
        }

        long end = System.currentTimeMillis();

        System.out.println("--->" + (end - start));

        threadPool.shutdown();
    }

    class Data {
        private String  tid;
        private Boolean repeat;

        public Data(String tid, Boolean repeat) {
            this.tid = tid;
            this.repeat = repeat;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public Boolean getRepeat() {
            return repeat;
        }

        public void setRepeat(Boolean repeat) {
            this.repeat = repeat;
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
                Data data = dataMap.get(tid);
                if (data.getRepeat()) {
                    test2();
                } else {
                    this.processOrder(tid);
                }
            } else if (type == 3) {
                test3();
            } else {
                test4();
            }
            cdl.countDown();
        }

        private void test1() {
            /*
             * synchronized (doing) { while (doing.containsKey(tid)) { try {
             * doing.wait(); } catch (InterruptedException e) {
             * e.printStackTrace(); } } doing.put(tid, ""); }
             */

            this.processOrder(tid);

            /*
             * synchronized (doing) { doing.remove(tid); doing.notifyAll(); }
             */
        }

        private void test2() {
            NotifyObj notifyObj = null;
            synchronized (doing5) {
                notifyObj = doing5.get(tid);
                if (notifyObj == null) {
                    notifyObj = new NotifyObj();
                    doing5.put(tid, notifyObj);
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
            result.add("100" + (int) (Math.random() * NUM));
            //            result.add(String.valueOf(i));
            //            result.add("100");
            //            result.add("200");
            //            result.add("300");
        }
        return result;
    }

}
