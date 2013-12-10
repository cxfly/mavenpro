package com.cxfly.test;

import java.util.ArrayList;
import java.util.List;

public class MyTest2 {

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {

        List<Long> ids = new ArrayList<Long>();
        List<Long> ids2 = new ArrayList<Long>();

        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        for (int i = 0; i < 1000 * 10000; i++) {
            ids.add(new Long(i));
            //            ids2.add(new Long(i));
        }
        System.out.println(ids.size());
        System.out.println(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

}
