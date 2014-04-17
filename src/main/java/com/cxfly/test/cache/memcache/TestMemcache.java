package com.cxfly.test.cache.memcache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class TestMemcache {
	public static void main(String[] args) throws Exception {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses("10.204.38.185:11211 10.204.38.186:11211"));
		MemcachedClient memcachedClient = builder.build();
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		for (int i = 0; i < 100; i++) {
			String key = String.valueOf(i);
			memcachedClient.set(key, 0, "Hello, xmemcached-" + i);
			String value = memcachedClient.get(key);
			System.out.println(key + " = " + value);
			 memcachedClient.delete(key);
			 value = memcachedClient.get(key);
			 System.out.println(key + "=" + value);
		}
		// close memcached client
		memcachedClient.shutdown();
	}
}
