package com.cxfly.test.collection.map;

import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public class TestHashMap {
    Map map = new SimpleMap(4);

    public static void main(String[] args) {
        TestHashMap t = new TestHashMap();
        t.doTest();
    }

    public void doTest() {
        map.put("AaAa", "A1");
        map.put("BBBB", "B1");
        map.put("AaBB", "C1");
        Thread t1 = new Thread() {
            @Override
            public void run() {
                map.put("CaDB", "A2");
                map.put("BBAa", "D1");
                
            }
        };
        Thread t2 = new Thread() {
            @Override
            public void run() {
                map.put("CaCa", "B2");
               System.out.println( map.get("BBAa"));
            }
        };

        t1.start();
        t2.start();
    }
}
