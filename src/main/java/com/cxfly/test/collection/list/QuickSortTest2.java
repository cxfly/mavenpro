package com.cxfly.test.collection.list;

import java.util.Arrays;

public class QuickSortTest2 {
	public static void main(String[] args) {
		int[] arr = { 4, 2, 6, 8, 1, 3, 7, 5 };
//		arr = QuickSortTest.generateData(30);
		System.out.println(Arrays.toString(arr));
//		sort(arr, 0, arr.length - 1);
		sort(arr);
		System.out.println(Arrays.toString(arr));
	}

	private static void sort(int[] arr, int low, int high) {
		if (low < high) {
			int mid = partition(arr, low, high);
			sort(arr, low, mid - 1);
			sort(arr, mid + 1, high);
		}
	}

	private static int partition(int[] arr, int low, int high) {
		for (int i = low, k = arr[high]; i < high; i++) {
			if (arr[i] < k) {
				swap(arr, low, i);
				low++;
			}
		}
		swap(arr, low, high);
		return low;
	}

	private static void swap(int[] array, int a, int b) {
		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}

	/***
	 * <pre>
	 * 
	 * </pre>
	 */
	
	
	static void sort2(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length-i-1; j++) {
				if (arr[j]>arr[j+1]) {
					swap(arr, j, j+1);
				}
			}
		}
	}
	
	static void sort(int[] arr){
		for (int i = 0; i < arr.length; i++) {
			int min = i;
			for (int j = i+1; j < arr.length; j++) {
				if (arr[j]< arr[min]) {
					min = j;
				}
			}
			swap(arr, i, min);
		}
	}
}
