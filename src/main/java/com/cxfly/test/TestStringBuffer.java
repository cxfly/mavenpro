package com.cxfly.test;

public class TestStringBuffer {
	public static void main(String[] args) {
		StringBuffer buf = new StringBuffer();
		buf.append("@").append("234324").append("@").append("zaa");
		String subSequence = buf.substring(2, buf.length());
		System.out.println(subSequence);
	}
}
