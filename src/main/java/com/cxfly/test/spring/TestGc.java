package com.cxfly.test.spring;

import java.util.ArrayList;
import java.util.List;

public class TestGc {
	public static void main(String[] args) throws Exception {
		Thread.sleep(1000*10);
		List<byte[]> list = new ArrayList<byte[]>();
		for (int i = 0; i < 1000000000; i++) {
			list.add( new byte[1000]);
		}
	}
}
