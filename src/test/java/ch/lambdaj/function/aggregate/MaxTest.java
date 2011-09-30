// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import static ch.lambdaj.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

/** @author Luca Marrocco */
public class MaxTest {

	@Test
	public void testLesserFirst() {
		assertThatMax(1, 2, is(equalTo(2)));
		assertThatMax(2, 3, is(equalTo(3)));
		assertThatMax(3, 4, is(equalTo(4)));
		assertThatMax(1, null, is(equalTo(1)));
	}

	@Test
	public void testGreaterFirst() {
		assertThatMax(4, 3, is(equalTo(4)));
		assertThatMax(3, 2, is(equalTo(3)));
		assertThatMax(2, 1, is(equalTo(2)));
		assertThatMax(null, 1, is(equalTo(1)));
	}

}
