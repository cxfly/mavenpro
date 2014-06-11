package com.cxfly.test.cache.memcache;

import java.util.concurrent.TimeoutException;

import com.cxfly.test.Order;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class TestMemcache {
	public static void main(String[] args) throws Exception {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses("10.204.38.185:11211 10.204.38.186:11211"));
		builder.setCommandFactory(new BinaryCommandFactory());//use binary protocol 
		builder.setConnectTimeout(1000);
		MemcachedClient memcachedClient = builder.build();
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		
		Order order = new Order(System.currentTimeMillis(), "No234234");
		
		objTest(memcachedClient, order);
		
//		randomTest(memcachedClient);
		// close memcached client
		memcachedClient.shutdown();
	}

	private static void objTest(MemcachedClient memcachedClient, Order order) throws TimeoutException,
			InterruptedException, MemcachedException {
		memcachedClient.set("abc", 0, order);
//		System.out.println(memcachedClient.add("abc", 0, order));
		System.out.println(memcachedClient.get("abc"));
	}

	private static void randomTest(MemcachedClient memcachedClient) throws TimeoutException, InterruptedException,
			MemcachedException {
		for (int i = 0; i < 100; i++) {
			String key = String.valueOf(i);
			memcachedClient.set(key, 10, "xmemcached-" + i);
			
			String value = memcachedClient.get(key);
			System.out.println(key + " = " + value);
//			 memcachedClient.delete(key);
			 value = memcachedClient.get(key);
			 System.out.println(key + "=" + value);
		}
	}
}
