// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Andrey Belyaev
 */
public class MultithreadTestMain {
	private static final int INNER_LOOP_NUMBER = 100;
	private static final int TASK_NUMBER = 50000;


	public static class SomeClass {
		private String stringProperty;
		private Double doubleProperty;

		public void setStringProperty(String stringProperty) {
			this.stringProperty = stringProperty;
		}

		public SomeClass(String stringProperty, Double doubleProperty) {
			super();
			this.stringProperty = stringProperty;
			this.doubleProperty = doubleProperty;
		}

		public String getStringProperty() {
			return stringProperty;
		}

		public void setDoubleProperty(Double doubleProperty) {
			this.doubleProperty = doubleProperty;
		}

		public Double getDoubleProperty() {
			return doubleProperty;
		}
	}

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(30);

		Collection<Callable<Object>> tasks = new LinkedList<Callable<Object>>();

		for (int i = 0; i < TASK_NUMBER; i++) {
			final List<SomeClass> list = Arrays.asList(new SomeClass("" + i,
					i / 10D), new SomeClass("" + i + i, (i + 10) / 10D),
					new SomeClass("" + i * 2, (i + 20) / 10D));
			final Integer num=i;
			tasks.add(new Callable<Object>() {
				public Object call() throws Exception {
					try{
						for(int j=0;j<INNER_LOOP_NUMBER;j++){
							List<String> strings = extract(list, on(SomeClass.class).getStringProperty());
							Double sum = sum(list, on(SomeClass.class).getDoubleProperty());
							System.out.println(num+" : "+strings.size() + " : " + sum);
						}
					}catch(Exception e){
						e.printStackTrace();
						System.exit(1);
					}
					System.gc();
					return null;
				}
			});
		}

		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.exit(0);
	}
}
