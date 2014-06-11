package com.cxfly.test.thread.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class TestCalculatorTask {
	public static void main(String[] args) throws Exception {
		long s1 = System.currentTimeMillis();
		long length = 1000000000;
		ForkJoinPool forkJoinPool = new ForkJoinPool(8);
		Future<Long> result = forkJoinPool.submit(new CalculatorTask(0L, length));
		System.out.println("result : " + result.get() + ", expect: " + 499999999500000000L);
		long s2 = System.currentTimeMillis();
		System.out.println("cost time: " + (s2 - s1));
		
		long ret = 0;
		for (long i = 1; i < length; i++) {
			ret +=i;
		}
		long s3 = System.currentTimeMillis();
		System.out.println((s3-s2 ) + ", ret:"  + ret);
		
	}
}
