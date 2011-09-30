// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import static ch.lambdaj.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

/** @author Luca Marrocco */
public class SumTest {

	@Test
	public void testSumNull() {
		assertThatSum(null, null, is(equalTo(0.0)));
	}

	@Test
	public void testSumNumberAndInteger() {
		assertThatSum(1, 1, is(equalTo(2)));
		assertThatSum(1, null, is(equalTo(1)));
		assertThatSum(null, 1, is(equalTo(1)));
	}

	@Test
	public void testSumNumberAndLong() {
		assertThatSum(1l, 1l, is(equalTo(2l)));
		assertThatSum(1l, null, is(equalTo(1l)));
		assertThatSum(null, 1l, is(equalTo(1l)));
	}

	@Test
	public void testSumNumberAndFloat() {
		assertThatSum(1f, 1f, is(equalTo(2f)));
		assertThatSum(1f, null, is(equalTo(1f)));
		assertThatSum(null, 1f, is(equalTo(1f)));
	}

	@Test
	public void testSumNumberAndDouble() {
		assertThatSum(1d, 1d, is(equalTo(2d)));
		assertThatSum(1d, null, is(equalTo(1d)));
		assertThatSum(null, 1d, is(equalTo(1d)));
	}
}