package com.cxfly.test.classload;

public class Parent {
	public static final String str = "abc_constants";
	public static final int s = 1;
	public static int d = 1;

	static {
		System.out.println("Parent 1000");
	}
	private static int c = getC();

	public int tt = getT();

	public Parent() {
		super();
		System.out.println("Parent() 111122222");
	}

	private int getT() {
		System.out.println("parent.getT() getT333333");
		return 9;
	}

	private static int getC() {
		System.out.println("Parent 1111111");
		return 11;
	}

}
