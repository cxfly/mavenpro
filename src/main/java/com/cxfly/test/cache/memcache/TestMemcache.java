package com.cxfly.test.cache.memcache;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.cxfly.test.Order;

@SuppressWarnings("unused")
public class TestMemcache {
	private static final int STEP = 10000 * 100;
	private static final int TIMES = 10;
	// private static final String HOSTS =
	// "10.204.38.185:11211 10.204.38.186:11211";
	private static final String HOSTS = "10.204.38.186:11211";

	private static CountDownLatch latch = new CountDownLatch(STEP * TIMES);
	private static AtomicInteger atom = new AtomicInteger(STEP * TIMES);

	public static void main(String[] args) throws Exception {
		// Order order = new Order(System.currentTimeMillis(), "No234234");
		//
		// // objTest(memcachedClient, order);
		//
		// randomTest(memcachedClient);
		// // close memcached client

		long s1 = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < TIMES; i++) {
			CacheWork work = new CacheWork(i * STEP + 1, (i + 1) * STEP);
			threadPool.execute(work);
		}
		long ct = 0;
		while ((ct = atom.intValue()/*latch.getCount()*/) > 0) {
			System.out.println(ct);
			Thread.sleep(30000);
		}

//		latch.await();
		long s2 = System.currentTimeMillis();
		System.out.println("finish test, cost time: " + (s2 - s1) / 1000);
		threadPool.shutdown();
	}

	private static void objTest(MemcachedClient memcachedClient, Order order) throws TimeoutException,
			InterruptedException, MemcachedException {
		memcachedClient.set("abc", 0, order);
		// System.out.println(memcachedClient.add("abc", 0, order));
		Object object = memcachedClient.get("abc");
		System.out.println(object);
	}

	private static void randomTest(MemcachedClient memcachedClient) throws TimeoutException, InterruptedException,
			MemcachedException {
		int step = 1;
		for (int i = 0; i < 10000 * 1000; i++) {
			String key = String.valueOf(i);
			memcachedClient.set(key, 0, i);
			// if ((i&(65535)) == 0) {
			// System.out.println(key + " = " + memcachedClient.get(key) );
			// System.out.println(step + " = " +
			// memcachedClient.get(String.valueOf(step)) );
			// step+=11;
			// }
			// memcachedClient.delete(key);
		}
		System.out.println("------------------------------------------");
		Random r = new Random(242342);
		for (int i = 0; i < 10000; i++) {
			long k = r.nextInt(10000 * 100);
			System.out.println(k + " = " + memcachedClient.get(String.valueOf(k)));
		}
	}

	static class CacheWork implements Runnable {

		private int start;
		private int end;

		public CacheWork(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public void run() {
			try {
				System.out.println("[" + start + ", " + end + "]");
				MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(HOSTS));
//				builder.setCommandFactory(new BinaryCommandFactory());
				builder.setConnectTimeout(1000);
				MemcachedClient memcachedClient = builder.build();
				builder.setSessionLocator(new KetamaMemcachedSessionLocator());

				for (int i = start; i <= end; i++) {
//					memcachedClient.set("k" + i, 0, i);
					memcachedClient.get("k" + i);
//					latch.countDown();
					atom.decrementAndGet();
				}
				memcachedClient.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
