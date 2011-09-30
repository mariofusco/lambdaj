// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import static ch.lambdaj.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

/** @author Luca Marrocco */
public class ConcatTest {

	@Test
	public void testConcatStrings() {
		assertThatConcat("", "", is(equalTo("")));
		assertThatConcat("a", "a", is(equalTo("a, a")));
	}

	@Test
	public void testConcatStringAndNumber() {
		assertThatConcat("a", "1", is(equalTo("a, 1")));
		assertThatConcat("1", "a", is(equalTo("1, a")));
	}

	@Test
	public void testConcatStringAndNull() {
		assertThatConcat("", null, is(equalTo("")));
		assertThatConcat(null, "", is(equalTo("")));
	}

	@Test
	public void testConcatNull() {
		assertThatConcat(null, null, is(equalTo("")));
	}

}
