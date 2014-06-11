package com.cxfly.test;

import java.util.Arrays;

public class TestRefence {

	public static void main(String[] args) {
		Object[] array = new Object[10];
		int tid = 100;
		String name = "ZhangSan";
		ShareData sharedData = new ShareData(array, tid, name);
		WorkerThread work = new WorkerThread(sharedData);
		Thread t1 = new Thread(work, "T1");
		Thread t2 = new Thread(work, "T2");

		t1.start();
		t2.start();

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class WorkerThread implements Runnable {
		private ShareData sharedData;

		public WorkerThread(ShareData sharedData) {
			super();
			this.sharedData = sharedData;
		}

		@Override
		public void run() {
			doTest();
		}

		private void doTest() {
			String name = Thread.currentThread().getName();
			if ("T1".equals(name)) {
				Object data = this.sharedData;
				System.out.println(name + ", sharedata = " + data);
				System.out.println(name + ", data = " + this.sharedData);
			} else {
				this.sharedData = null;
			}
			System.out.println(name + ", data = " + this.sharedData);
		}

	}

	static class ShareData {
		public ShareData(Object[] array, int tid, String name) {
			super();
			this.array = array;
			this.tid = tid;
			this.name = name;
		}

		private volatile Object[] array;
		private int tid;
		private String name;

		public Object[] getArray() {
			return array;
		}

		public void setArray(Object[] array) {
			this.array = array;
		}

		public int getTid() {
			return tid;
		}

		public void setTid(int tid) {
			this.tid = tid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "ShareData [array=" + Arrays.toString(array) + ", tid=" + tid + ", name=" + name + "]";
		}
		
	}

}
