package com.cxfly.test.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			
		}
	}
	
	
	private void fun() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("/com/cxfly/test/spring/springCfg.xml");
		ClassA classA = (ClassA) ctx.getBean("classA");
		System.out.println(classA);
		ClassB classB = (ClassB) ctx.getBean("classB");
		System.out.println(classB);
	}
}
