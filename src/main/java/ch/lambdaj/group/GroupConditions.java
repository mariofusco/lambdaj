// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * Defines a list of GroupConditions.
 * Extending it makes easy to create a simple DSL that creates GroupConditions for some specific objects of your domain.
 * @author Mario Fusco
 */
public class GroupConditions extends LinkedList<StringGroupCondition> {

	private static final long serialVersionUID = 1L;

    /**
     * Adds a GroupCondition the groups on the property with the given name
     * @param by The name of property to be used in the new GroupCondition
     */
	public void by(String by) {
		add(new StringGroupCondition(by));
	}

    /**
     * Sets an alias for the groups created using this condition
     * @param alias The alias to be set
     */
	public void as(String alias) {
		getLast().as(alias);
	}

    /**
     * Adds the value of the property with the given name as a header of the groups produced by applying this Condition
     * @param property The name of the property
     */
	public void head(String property) {
		getLast().head(property);
	}

    /**
     * Adds the value of the property with the given name as a header with the given alias of the groups produced by applying this Condition
     * @param property The name of the property
     * @param alias The alias used as name in the group header
     */
	public void head(String property, String alias) {
		getLast().head(alias, property);
	}
}
