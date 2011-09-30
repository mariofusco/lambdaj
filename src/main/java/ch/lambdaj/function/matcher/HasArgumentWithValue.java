// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.matcher;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import org.hamcrest.*;

import ch.lambdaj.function.argument.*;

/**
 * A matcher that returns true if the value resulting from the evaluation af the given argument matches the given matcher.
 * @author Mario Fusco
 */
public final class HasArgumentWithValue<T, A> extends LambdaJMatcher<T> {
	
    private final Argument<A> argument;
    private final Matcher<?> value;

    private HasArgumentWithValue(Argument<A> argument, Matcher<?> value) {
        this.argument = argument;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
	public boolean matches(Object item) {
		if (argument == null) return false;
        try {
            return value.matches(argument.evaluate(item));
        } catch (Exception e) {
            return false;
        } 
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("hasArgument(");
        description.appendValue(argument.getInkvokedPropertyName());
        description.appendText(", ");
        description.appendDescriptionOf(value);
        description.appendText(")");
    }

    /**
     * Creates an hamcrest matcher that is evalued to true accordingly to the value of the passed argument
     * @param argument The boolean argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method that has to be matched
     * @return The hamcrest matcher that is evalued to true accordingly to the value of the passed argument
     */
    @Factory
     public static <T> HasArgumentWithValue<T, Boolean> havingValue(Boolean argument) {
    	return havingValue(argument, BOOLEAN_MATCHER);
    }
    
    /**
     * Creates an hamcrest matcher that is evalued to true if the value of the given argument satisfies
     * the condition defined by the passed matcher.
     * @param argument The argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method that has to be matched
     * @param matcher The matcher against which the value of the given argument has to be compared
     * @return The hamcrest matcher that is evalued to true if the value of the passed argument matches the given matcher
     */
    @Factory
    public static <T, A> HasArgumentWithValue<T, A> havingValue(A argument, Matcher<?> matcher) {
    	return new HasArgumentWithValue<T, A>(actualArgument(argument), matcher);
    }

    private static final BooleanMatcher BOOLEAN_MATCHER = new BooleanMatcher();
    private static class BooleanMatcher extends BaseMatcher<Boolean> {
        /**
         * {@inheritDoc}
         */
		public boolean matches(Object item) {
			return (Boolean)item;
		}
        /**
         * {@inheritDoc}
         */
		public void describeTo(Description description) { }
    }
}
