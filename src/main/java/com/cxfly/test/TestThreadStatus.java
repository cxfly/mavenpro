package com.cxfly.test;

public class TestThreadStatus {

	static class Worker implements Runnable {

		@Override
		public void run() {
			String tName = Thread.currentThread().getName();
			System.out.println(tName + " is executing!");
			if ("t1".equals(tName)) {
				try {
					synchronized (this) {
//						wait();
						System.out.println();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				synchronized (this) {
					notify();
				}
			}
			System.out.println(" write data ");
			System.out.println(tName + " exist!");
		}
	}

	public static void main(String[] args) {
		Worker worker = new Worker();
		Thread t1 = new Thread(worker, "t1");
		Thread t2 = new Thread(worker, "t2");
		t1.start();
		t2.start();

		synchronized (worker) {
			interrupt(worker, t1);
		}

		System.out.println("main thread exit.");
	}

	private static void interrupt(Worker worker, Thread t1) {
		worker.notify();
		Thread t = t1;
		t1 = null;
		t.interrupt();
	}
}