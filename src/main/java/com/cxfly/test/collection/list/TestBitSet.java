package com.cxfly.test.collection.list;

import java.util.BitSet;

public class TestBitSet {
	public static void main(String[] args) {
//		long[] words = new  long[10000*1000*10];
//		BitSet valueOf = BitSet.valueOf(words);
		
		BitSet bitSet = new BitSet();
		
		int size = 1000000000;
		bitSet.set(size);
		System.out.println(bitSet.size());
		
	}
}
