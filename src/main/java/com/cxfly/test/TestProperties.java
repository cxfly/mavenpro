package com.cxfly.test;

public class TestProperties {
    private static final int a=  110;
    static String as = "abc";
    
    static{
        as = "ddddc";
    }
    
    public static void main(String[] args) {
        System.out.println(System.getProperty("tiger"));
    }
}
