package com.cxfly.test.collection.map;

import java.util.concurrent.ConcurrentMap;




@SuppressWarnings("rawtypes")
public class TestConcurrentHashMap {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ConcurrentMap map = new SimpleConcurrentHashMap(4, 0.75f, 4419);
        
        map.put("a", "test");
        System.out.println(map.size());
        
    }
}
