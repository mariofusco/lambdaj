// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static org.junit.Assert.*;

import org.hamcrest.*;

import ch.lambdaj.function.aggregate.*;

import static java.util.Arrays.asList;

/** @author Luca Marrocco */
@SuppressWarnings("unchecked")
public class Assert {
	public static final <T extends Number> void assertThatSum(Number op1, T op2, Matcher<T> matcher) {
		Sum summer = new Sum();
		T result = (T) summer.aggregate(op1, op2);
		assertThat(result, matcher);
	}

	public static final <T extends Comparable> void assertThatMin(T op1, T op2, Matcher<T> matcher) {
		Min<T> minFinder = new Min<T>();
		T result = (T) minFinder.aggregate(op1, op2);
		assertThat(result, matcher);
	}

	public static final <T extends Comparable> void assertThatMax(T op1, T op2, Matcher<T> matcher) {
		Max<T> maxFinder = new Max<T>();
		T result = (T) maxFinder.aggregate(op1, op2);
		assertThat(result, matcher);
	}

	public static final void assertThatConcat(String op1, String op2, Matcher<String> matcher) {
		Concat concatenator = new Concat();
        String result = (String) concatenator.aggregate(asList(op1, op2).iterator());
		assertThat(result, matcher);
	}
}
