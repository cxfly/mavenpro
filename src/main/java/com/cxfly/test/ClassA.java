package com.cxfly.test;

public class ClassA {
    public int        a = getA();

    {
        System.out.println("block :" + 30001);
    }
    static {
        System.out.println("static :" + 3000);
    }

    public static int b = getB();

    private int getA() {
        System.out.println("getA :" + 111);
        return 111;
    }

    private static int getB() {
        System.out.println("getB :" + 222);
        return 222;
    }
}
