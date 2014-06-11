package com.cxfly.test.classload;

public class Sub extends Parent {

    public  static int a = 2;
    public static int b = getb();
    
    static{
    	d = 3;
    	b = 22;
        System.out.println("Sub 2000");
    }
    
    public Sub() {
    	super();
        System.out.println("Sub 2001");
    }

    private static int getb() {
        System.out.println("Sub 2003 a=" + a);
        return 11;
    }
    
    

}
