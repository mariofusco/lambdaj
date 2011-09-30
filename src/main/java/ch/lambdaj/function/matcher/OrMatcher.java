// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.matcher;

import org.hamcrest.*;

/**
 * A matcher that logically combines a set of matchers returning true if at least one of them satisfies its own condition.
 * @author Mario Fusco
 */
public final class OrMatcher<T> extends LambdaJMatcher<T> {
	
	private final Matcher<T>[] matchers;

	private OrMatcher(Matcher<T>... matchers) {
		this.matchers = matchers;
	}
	
    /**
     * {@inheritDoc}
     */
	public boolean matches(Object item) {
		for (Matcher<T> matcher : matchers) { if (matcher.matches(item)) return true; }
		return false;
	}

    /**
     * Creates an or matcher combining all the passed matchers
     * @param matchers The matchers to be put in or
     * @return A matcher that return true if at least one of the matchers return true
     */
    @Factory
    public static <T> OrMatcher<T> or(Matcher<T>... matchers) {
    	return new OrMatcher<T>(matchers);
    }
}