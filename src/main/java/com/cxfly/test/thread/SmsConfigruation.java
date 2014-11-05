package com.cxfly.test.thread;

public class SmsConfigruation {
	private static  boolean flag;
	static int size = 10;

	static {
//		new Thread() {
//			public void run() {
//				try {
//					Thread.sleep(4000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				setFlag(true);
//			}
//		}.start();
		String str = "10";
		str  = "true";
		flag = Boolean.valueOf(str);
		str = "999";
		size = Integer.valueOf(str);
		System.out.println(size);

	}

	public static void setFlag(boolean f) {
		flag = f;
	}

	public static boolean getFlag() {
		return flag;
	}
	
	public static int getSize() {
		return size;
	}
}
