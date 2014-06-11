package com.cxfly.test.cache.redis;

import redis.clients.jedis.Jedis;

//jedis
public class TestRedis {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("10.204.38.185",6379);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 3; i++) {
			String key = "n" + i;
			@SuppressWarnings("unused")
			String result = jedis.set(key, "Hello,Redis-" + i);
			System.out.println(key + " = " + jedis.get(key));
		}
		jedis.set("foo", "李四");
		System.out.println(jedis.get("foo"));
		long end = System.currentTimeMillis();
		System.out.println("Simple SET: " + ((end - start) / 1000.0)
				+ " seconds");
		jedis.disconnect();
	}
}
