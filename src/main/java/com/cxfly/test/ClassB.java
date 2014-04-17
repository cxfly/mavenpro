package com.cxfly.test;

public class ClassB {

    public static class Holder {
        static ClassB b = new ClassB();
    }

    private ClassB() {
        System.out.println(".......ClassB().......");
    }

    public static ClassB getInstance() {
        System.out.println(".......getInstance().......");
        return Holder.b;
    }

}
