package com.cxfly.test.classload;

public class Sun extends Parent {

    public final static int a = 2;
    public static int b = getb();
    
    static{
        System.out.println("2000");
    }
    
    public Sun() {
        super();
        System.out.println("2001");
    }

    private static int getb() {
        System.out.println("2003 a=" + a);
        return 11;
    }
    
    

}
