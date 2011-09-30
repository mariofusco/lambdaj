// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.matcher;

import static ch.lambdaj.util.IntrospectionUtil.*;

import org.hamcrest.*;

/**
 * A matcher that returns true if the value af the named property matches the given matcher.
 * @author Mario Fusco
 */
public class HasNestedPropertyWithValue<T> extends LambdaJMatcher<T> {

    private final String propertyName;
    private final Matcher<?> value;

    /**
     * Creates a matcher that returns true if the value af the named property matches the given matcher
     * @param propertyName The name of the property
     * @param value The value to be mathced
     */
    public HasNestedPropertyWithValue(String propertyName, Matcher<?> value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
	public boolean matches(Object item) {
        try {
            return value.matches(getPropertyValue(item, propertyName));
        } catch (Exception e) {
            return false;
        } 
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("hasProperty(");
        description.appendValue(propertyName);
        description.appendText(", ");
        description.appendDescriptionOf(value);
        description.appendText(")");
    }

    /**
     * Creates a matcher that returns true if the value af the named property matches the given matcher
     * @param propertyName The name of the property
     * @param value The value to be mathced
     * @return  A matcher that returns true if the value af the named property matches the given matcher
     */
    @Factory
    public static <T> Matcher<T> hasNestedProperty(String propertyName, Matcher<?> value) {
        return new HasNestedPropertyWithValue<T>(propertyName, value);
    }
}
