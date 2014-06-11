package com.cxfly.test.thread.forkjoin;

import java.util.concurrent.RecursiveTask;

public class CalculatorTask extends RecursiveTask<Long> {
	private static final long serialVersionUID = -6871229672037251970L;

	private static final int THRESHOLD = 100;
	private long start;
	private long end;

	public CalculatorTask(long start, long end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		long sum = 0;
		if ((start - end) < THRESHOLD) {
			for (long i = start; i < end; i++) {
				sum += i;
//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		} else {
			long middle = (start + end) / 2;
			CalculatorTask left = new CalculatorTask(start, middle);
			CalculatorTask right = new CalculatorTask(middle + 1, end);
			
//			left.fork();
//			right.fork();
			
//			invokeAll(left,right);

			sum = left.join() + right.join();
		}
		return sum;
	}

}
