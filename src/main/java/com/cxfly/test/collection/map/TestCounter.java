package com.cxfly.test.collection.map;

import java.util.HashMap;

public class TestCounter {

	public static void main(String[] args) {
		// 源字符串
		String source = "my name is name me and your name is her 53534 3535 34 5 6 3534 7375 44 irst h er 243ZZj werwrwe 3453453Z testNaive(source, loop); long duration = 0;";
		// 计时,单位: 微秒
		long startTime = 0;
		long endTime = 0;
		long duration = 0;
		// 测试次数
		int loop = 1 * 100000;

		System.out.println(loop + " 次循环:");
		startTime = System.nanoTime();
		testNaive(source, loop);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("新手级计数器: " + duration);
		//
		startTime = System.nanoTime();
		testBetter(source, loop);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("入门级计数器: " + duration);
		//
		startTime = System.nanoTime();
		testEfficient2(source, loop);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("卓越级计数器: " + duration);

		startTime = System.nanoTime();
		testEfficient(source, loop);
		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("超卓级计数器: " + duration);
	}

	// 新手级计数器
	public static void testNaive(String source, int loop) {
		if (null == source) {
			return;
		}
		//
		String[] words = source.split(" ");
		for (int i = 0; i < loop; i++) {
			testNaive(words);
		}
	}

	public static void testNaive(String[] words) {
		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		for (String w : words) {
			if (counter.containsKey(w)) {
				int oldValue = counter.get(w);
				counter.put(w, oldValue + 1);
			} else {
				counter.put(w, 1);
			}
		}
	}

	// 可变Integer
	public static final class MutableInteger {
		private int val;

		public MutableInteger(int val) {
			this.val = val;
		}

		public int get() {
			return this.val;
		}

		public void set(int val) {
			this.val = val;
		}

		// 为了方便打印
		public String toString() {
			return Integer.toString(val);
		}
	}

	// 入门级计数器
	public static void testBetter(String source, int loop) {
		if (null == source) {
			return;
		}
		//
		String[] words = source.split(" ");
		for (int i = 0; i < loop; i++) {
			testBetter(words);
		}
	}

	public static void testBetter(String[] words) {
		HashMap<String, MutableInteger> counter = new HashMap<String, MutableInteger>();
		for (String w : words) {
			if (counter.containsKey(w)) {
				MutableInteger oldValue = counter.get(w);
				oldValue.set(oldValue.get() + 1); // 因为是引用,所以减少了一次HashMap查找
			} else {
				counter.put(w, new MutableInteger(1));
			}
		}
	}

	// 卓越级计数器
	public static void testEfficient(String source, int loop) {
		if (null == source) {
			return;
		}
		//
		String[] words = source.split(" ");
		for (int i = 0; i < loop; i++) {
			testEfficient(words);
		}
	}

	public static void testEfficient(String[] words) {
		HashMap<String, MutableInteger> counter = new HashMap<String, MutableInteger>();
		for (String w : words) {
			MutableInteger initValue = new MutableInteger(1);
			// 利用 HashMap 的put方法弹出旧值的特性
			MutableInteger oldValue = counter.put(w, initValue);
			if (oldValue != null) {
				initValue.set(oldValue.get() + 1);
			}
		}
	}

	private static void testEfficient2(String source, int loop) {
		if (null == source) {
			return;
		}
		//
		String[] words = source.split(" ");
		for (int i = 0; i < loop; i++) {
			testEfficient2(words);
		}
	}
	
	public static void testEfficient2(String[] words) {
		HashMap<String, MutableInteger> counter = new HashMap<String, MutableInteger>();
		for (String w : words) {
			MutableInteger mutableInteger = counter.get(w);
			if (mutableInteger != null) {
				mutableInteger.set(mutableInteger.get() + 1);
			} else {
				counter.put(w, new MutableInteger(1));
			}
		}
	}
}