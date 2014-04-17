package com.cxfly.test.classload;

public class Parent {
    public final static int d = 1;

    static{
        System.out.println("1000");
    }
    private static int       c = getC();
    
    public Parent() {
        super();
        System.out.println("111122222");
    }

    private static int getC() {
        System.out.println("1111111");
        return 11;
    }

}
