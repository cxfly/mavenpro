package com.cxfly.test.thread;

public class TestThreadCreate {
	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Hello");
			}
		};
		
		Thread t = new Thread(runnable);
		t.start();
		
		System.out.println(" world ");
		t.start();
		
		System.out.println(" end ");
	}
}
