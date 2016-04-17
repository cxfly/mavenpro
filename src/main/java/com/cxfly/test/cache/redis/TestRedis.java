package com.cxfly.test.cache.redis;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import redis.clients.jedis.Jedis;

//jedis
public class TestRedis {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Jedis jedis = new Jedis("10.204.38.185",6379);
		List<String> time = jedis.time();
		long start = System.currentTimeMillis();
		System.out.println(time.get(0) + "." + time.get(1));
		System.out.println(start);
		System.out.println(Calendar.getInstance().toString());
		
		for (int i = 0; i < 30000; i++) {
			String key = "n" + i;
//			@SuppressWarnings("unused")
//			String result = jedis.set(key, "Hello,Redis-" + i);
//			jedis.get(key);
//			System.out.println(key + " = " + jedis.get(key));
		}
		
		
		jedis.set("foo", "李四");
		System.out.println(jedis.get("foo"));
		
		
		long end = System.currentTimeMillis();
		System.out.println("Simple SET: " + ((end - start) / 1000.0)
				+ " seconds");
		
		
		jedis.lpush("key1", String.valueOf(System.currentTimeMillis()));
		jedis.lpush("key1", String.valueOf(System.currentTimeMillis()));
		jedis.lpush("key1", String.valueOf(System.currentTimeMillis()));
		List<String> lrange = jedis.lrange("key1", 0, -1);
		System.out.println(lrange);
		jedis.disconnect();
	}
}
