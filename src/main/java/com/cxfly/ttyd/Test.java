package com.cxfly.ttyd;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	public static void main(String[] args) {

		AtomicInteger atom = new AtomicInteger();
		WeakReference<AtomicInteger> weakCar = new WeakReference<AtomicInteger>(atom);
		int i = 0;

		while (true) {
			System.out.println("here is the strong reference 'car' " + atom);
			atom = null;
			if (weakCar.get() != null) {
				i++;
				System.out.println("Object is alive for " + i + " loops - " + weakCar);
			} else {
				System.out.println("Object has been collected. "  + i);
				break;
			}
		}
	}

}
