// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.matcher;

import org.hamcrest.*;


/**
 * A Matcher that extends the Hamcrest BaseMatcher by adding two fluent interface style methods that
 * allow to logically combine two matchers.
 * @author Mario Fusco
 */
public abstract class LambdaJMatcher<T> extends BaseMatcher<T> {

    /**
     * Creates an and matcher combining this matcher and the given one
     * @param matcher The matcher to be put in and with this one
     * @return A matcher that return true if this matcher and the passed one return true
     */
	@SuppressWarnings("unchecked")
	public final LambdaJMatcher<T> and(Matcher<T> matcher) {
		return AndMatcher.and(this, matcher);
	}

    /**
     * Creates an or matcher combining this matcher and the given one
     * @param matcher The matcher to be put in or with this one
     * @return A matcher that return true if this matcher or the passed one return true
     */
	@SuppressWarnings("unchecked")
	public final LambdaJMatcher<T> or(Matcher<T> matcher) {
		return OrMatcher.or(this, matcher);
	}

    /**
     * {@inheritDoc}
     */
    public void describeTo(Description description) { }
}
