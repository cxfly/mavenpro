package com.cxfly.test.collection.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class ListTest {

    //    private final List<String> list         = new CopyOnWriteArrayList<String>();
    private final List<String> list         = new Vector<String>(20000000);
    //    private final List<String> list         = Collections.synchronizedList(new ArrayList<String>(20000000));
    //    private final List<String> list         = new ArrayList<String>();
    //    private final String[]     arr          = new String[20000001];

    private static final int   CPU_CORE_NUM = 40;

    public static void main(String[] args) {
        ListTest test = new ListTest();
        long start = System.currentTimeMillis();
        test.doTest();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    void doTest() {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(CPU_CORE_NUM);
        final AtomicInteger count = new AtomicInteger();
        List<Thread> tlist = new ArrayList<Thread>();
        for (int i = 0; i < CPU_CORE_NUM; i++) {
            Thread t = new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < 500000; j++) {
                        list.add(Thread.currentThread().getName() + "+" + j);
                        //                        arr[count.incrementAndGet()] = j + "";
                    }
                };
            };
            tlist.add(t);
            t.start();
        }

        while (!tlist.isEmpty()) {
            for (int i = 0; i < tlist.size(); i++) {
                Thread thread = tlist.get(i);
                if (!thread.isAlive()) {
                    tlist.remove(i);
                }
            }
        }

        System.out.println(count.intValue());
    }
}
