// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

/**
 * A GroupCondition that allows to group items based on the value they have on a given argument
 * @author Mario Fusco
 */
public class ArgumentGroupCondition<T> extends GroupCondition<Argument<?>> {
	
	private final Argument<T> groupBy;
	private final String groupName;

	ArgumentGroupCondition(T argument) {
		groupBy = actualArgument(argument);
		groupName = groupBy.getInkvokedPropertyName();
	}
	
    /**
     * {@inheritDoc}
     */
	protected String getGroupName() {
		return groupName;
	}

    /**
     * {@inheritDoc}
     */
    protected Object getGroupValue(Object item) {
        return groupBy.evaluate(item);
    }

    /**
     * Sets an alias for the groups created using this condition
     * @param alias The alias to be set
     * @return The GroupCondition itself in order to allow a fluent interface
     */
	@Override
	public ArgumentGroupCondition as(String alias) {
		return (ArgumentGroupCondition)super.as(alias);
	}

    /**
     * Adds the value of the given argument in the group's header
     * @param argument The argument
     * @return The ArgumentGroupCondition itself
     */
    public ArgumentGroupCondition head(Object argument) {
		Argument<?> actualArgument = actualArgument(argument);
		additionalProperties.put(actualArgument.getInkvokedPropertyName(), actualArgument);
		return this;
	}

    /**
     * Adds the value of the given argument in the group's header with the given alias
     * @param argument The argument
     * @param alias The alias
     * @return The ArgumentGroupCondition itself
     */
	public ArgumentGroupCondition head(Object argument, String alias) {
		additionalProperties.put(alias, actualArgument(argument));
		return this;
	}

    /**
     * {@inheritDoc}
     */
	protected String getAdditionalPropertyValue(String name, Object item) {
		return asNotNullString(additionalProperties.get(name).evaluate(item));
	}
}
