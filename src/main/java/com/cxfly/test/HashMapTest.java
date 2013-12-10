package com.cxfly.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HashMap并发导致CPU 100%，详见 http://coolshell.cn/articles/9606.html
 * 
 * @author lz
 */
public class HashMapTest {
    //    private final Map<String, String> map          = new ConcurrentHashMap<String, String>();
    private final Map<String, String> map          = new HashMap<String, String>();
    private static final int          CPU_CORE_NUM = 3;

    public static void main(String[] args) {
        HashMapTest test = new HashMapTest();
        test.doTest();
    }

    void doTest() {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(CPU_CORE_NUM);
        final AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < CPU_CORE_NUM; i++) {
            Thread t = new Thread() {
                @Override
                public synchronized void run() {
                    try {
                        cyclicBarrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < 10000; j++) {
                        map.put(String.valueOf(count.getAndIncrement()), "");
                    }
                };
            };
            t.start();
        }
    }
}
