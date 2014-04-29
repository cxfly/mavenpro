package com.cxfly.test;

import java.util.HashMap;
import java.util.Map;

public class TestMemoryEscape {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        for (int i = 0; i < 3; i++) {
            map.put(String.valueOf(i), new AAA());
            Thread.sleep(1);
        }
        
        Thread.sleep(1000*300);
//        System.out.println(map);
    }
    
    static class AAA{
    	
    }

}
