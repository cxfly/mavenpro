package com.cxfly.test.classload;

public class X {
	private static int a;

	public static void main(String[] args) {
		modify(a);
		System.out.println(a);
	}

	public static void modify(int a) {
		a++;
	}
}
