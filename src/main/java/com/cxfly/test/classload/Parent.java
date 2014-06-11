package com.cxfly.test.classload;

public class Parent {
	public static int d = 1;

	static {
		System.out.println("Parent 1000");
	}
	private static int c = getC();

	private int tt = getT();

	public Parent() {
		super();
		System.out.println("Parent 111122222");
	}

	private int getT() {
		System.out.println("parent getT333333");
		return 9;
	}

	private static int getC() {
		System.out.println("Parent 1111111");
		return 11;
	}

}
