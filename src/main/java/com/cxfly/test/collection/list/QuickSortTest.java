package com.cxfly.test.collection.list;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 快速排序算法Java实现
 * 
 * <pre>
 * 网上关于快速排序的算法原理和算法实现都比较多，不过java是实现并不多，而且部分实现很难理解，和思路有点不搭调。
 * 所以整理了这篇文章。如果有不妥之处还请建议。首先先复习一些基础。
 * 
 * 1、算法概念。
 * 快速排序（Quicksort）是对冒泡排序的一种改进。由C. A. R. Hoare在1962年提出。
 * 
 * 2、算法思想。
 * 通过一趟排序将要排序的数据分割成独立的两部分，其中一部分的所有数据都比另外一部分的所有数据都要小，
 * 然后再按此方法对这两部分数据分别进行快速排序，整个排序过程可以递归进行，以此达到整个数据变成有序序列。
 * 
 * 3、实现思路。
 * ①以第一个关键字 K 1 为控制字，将 [K 1 ,K 2 ,…,K n ] 分成两个子区，使左区所有关键字小于等于 K 1 ，
 * 	右区所有关键字大于等于 K 1 ，最后控制字居两个子区中间的适当位置。在子区内数据尚处于无序状态。 
 * ②把左区作为一个整体，用①的步骤进行处理，右区进行相同的处理。（即递归）
 * ③重复第①、②步，直到左区处理完毕
 * </pre>
 */
public class QuickSortTest {
	public static void main(String[] args) {
		int[] arr = { 6, 3, 7, 2, 8, 9, 5, 4 };
		arr = generateData(10);
		System.out.println(Arrays.toString(arr));
		quickSort2(arr, 0, arr.length - 1);
		System.out.println(Arrays.toString(arr));
	}

	private static void quickSort2(int[] array, int low, int high) {
		if (low < high) {
			int middle = partion(array, low, high);
			System.out.println("middle[" + middle + "]=" + array[middle]);
			quickSort2(array, low, middle - 1);
			quickSort2(array, middle + 1, high);
		}
	}

	// { 6, 3, 7, 2, 8, 9, 5, 4 }
	private static int partion(int[] array, int low, int high) {
		System.out.printf("arr: %s, low: %d, high: %d%n", Arrays.toString(array), low, high);
		int key = array[high];
		for (int i = low; i < high; i++) {
			if (array[i] <= key) {
				if (i != low) {
					System.out.printf("swap low: %d, selector: %d%n", array[low], array[i]);
					swap(array, low, i);
				}

				low++;
				System.out.printf("moved low to next indx: array[%d]=%d%n", low, array[low]);
			}
		}
		if (low < high) {
			System.out.printf("swap low: %d, high: %d%n", array[low], array[high]);
			swap(array, low, high);
		}
		return low;
	}

	static void swap(int[] x, int a, int b) {
		if (a != b) {
			int t = x[a];
			x[a] = x[b];
			x[b] = t;
		}
	}

	static void quickSort(int[] array, int from, int to) {
		if (from < to) {
			int key = array[to];
			int i = from - 1;
			for (int j = from; j < to; j++) {
				if (array[j] <= key) {
					i++;
					int tempValue = array[j];
					array[j] = array[i];
					array[i] = tempValue;
				}
			}
			array[to] = array[i + 1];
			array[i + 1] = key;
			quickSort(array, from, i);
			quickSort(array, i + 1, to);
		}
	}

	public static int[] generateData(int size) {
		int[] result = new int[size];
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0, length = result.length; i < length; i++) {
				result[i] = random.nextInt(100);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}

/**
 * <pre>
 * {6, 3, 7, 2, 8, 9, 5, 4} 
 * 3, 6, 7, 2, 8, 9, 5, 4 	3<-->6 
 * 3, 2, 7, 6, 8, 9, 5, 4 	2<-->6 
 * 3, 2, 4, 6, 8, 9, 5, 7 	4<-->7(low<--->high) 
 * low:4 partion: {3, 2}, 4, {6, 8, 9, 5, 7} 
 * ---------------------------------------- 
 * {3, 2} 
 * 2, 3
 * low<--->high
 * {6, 8, 9, 5, 7} 
 * 6, 8, 9, 5, 7 	6<-->6 
 * 6, 5, 9, 8, 7 	5<-->8 
 * 6, 5, 7, 8, 9 	7<-->9(low<--->high) 
 * low:7 partion: 4 {6, 5}, {8, 9}
 * </pre>
 */
