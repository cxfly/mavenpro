package com.cxfly.test.classload;

public class Test {
    public static void main(String[] args) throws Throwable {
//        System.out.println("xxx " + Sun.a);
//        System.out.println("xxx " + Sun.d);
//        System.out.println("xxx " + Sun.b);
//        new Sun();
        
//        Parent[] array = new Sun[10];
//    	new Sub();
    	
//    	Class.forName("com.cxfly.test.classload.Sub");// 会调静态块、静态变量
    	
//    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
//    	Class<?> myclass = cl.loadClass("com.cxfly.test.classload.Sub");
    	
//    	myclass.newInstance();
    	
//    	System.out.println(Sub.a);
//    	System.out.println(Sub.b);
    	
//    	Parent p = new  Parent();
    	Sub.init2();
    	
    }
}
