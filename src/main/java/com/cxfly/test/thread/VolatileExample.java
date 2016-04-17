package com.cxfly.test.thread;

public class VolatileExample extends Thread {
	// 设置类静态变量,各线程访问这同一共享变量
	private long a = 1;

	// 无限循环,等待flag变为true时才跳出循环
	public void run() {
		System.out.println(SmsConfigruation.getSize());
		while (!SmsConfigruation.getFlag()) {
			if (a++ == 1111111111111L) {
				System.out.println(a);
			}
		}
		System.out.println("exit...............");
	}

	public static void main(String[] args) throws Exception {
		
//		for (int i = 0; i < 100; i++) {
//			new VolatileExample().start();
//		}
		// sleep的目的是等待线程启动完毕,也就是说进入run的无限循环体了
//		Thread.sleep(1000);
//		SmsConfigruation.setFlag(true);
//		new SmsConfigruation();
//		System.out.println(SmsConfigruation.size);
		SmsConfigruation.getFlag();
	
	}
	
	
	
}