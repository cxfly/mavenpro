package com.cxfly.test.map;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class TreeMapTest {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		SortedMap treeMap = new TreeMap();
		treeMap.put(8, "1");
		treeMap.put(2, "1");
		treeMap.put(9, "1");
		treeMap.put(3, "1");
		treeMap.put(1, "1");
		
		treeMap = treeMap.tailMap(5);
		Set keySet = treeMap.keySet();
		for (Object object : keySet) {
			System.out.println(object );
		}
		
		System.out.println(treeMap.firstKey());
		
		
		
		
	}
}
