package com.cxfly.test;

import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TestRandom {
	
	public static void main(String[] args) throws Exception {
		long s1 = System.currentTimeMillis();
		SecureRandom result = null;
		try {
			// result = SecureRandom.getInstance("NativePRNG");
			result = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		InetAddress localHost = InetAddress.getLocalHost();
		System.out.println(localHost.toString());
		
		System.out.println(result.nextInt());
		
		long s2 = System.currentTimeMillis();
		long costTime = s2 - s1;
		System.out.println("random cost time: " + costTime + " ms");
	}
}
