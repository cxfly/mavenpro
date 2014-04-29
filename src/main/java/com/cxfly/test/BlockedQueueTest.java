package com.cxfly.test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockedQueueTest {
		@SuppressWarnings("rawtypes")
		public static void main(String[] args) throws Exception {
			LinkedBlockingQueue queue = new LinkedBlockingQueue();
			Object poll2 = queue.poll(); 
			System.out.println(poll2);
			Object poll = queue.poll(10, TimeUnit.SECONDS); 
			System.out.println(poll);
			
		}
	 
}
