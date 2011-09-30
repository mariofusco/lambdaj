// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.util.IntrospectionUtil.getPropertyValue;


/**
 * A GroupCondition that groups items based on the values they have on the named JavaBean property
 * @author Mario Fusco
 */
public class StringGroupCondition extends GroupCondition<String> {

	private final String groupBy;

    /**
     * Creates a GroupCondition that groups items based on the values they have on the given JavaBean property
     * @param groupBy The name of the property on which the items have to be grouped
     */
	public StringGroupCondition(String groupBy) {
		this(groupBy, null);
	}

    /**
     * Creates a GroupCondition that groups items based on the values they have on the given JavaBean property
     * @param groupBy The name of the property on which the items have to be grouped
     * @param alias The alias used for the groups created using this condition
     */
	public StringGroupCondition(String groupBy, String alias) {
		this.groupBy = groupBy;
		as(alias);
	}

	protected String getGroupName() {
		return groupBy;
	}
	
	protected Object getGroupValue(Object item) {
        return getPropertyValue(item, groupBy);
	}

    /**
     * Adds the value of the property with the given name as a header of the groups produced by applying this Condition
     * @param name The name of the property
     * @return This StringGroupCondition
     */
	public StringGroupCondition head(String name) {
		return head(name, name);
	}

    /**
     * Adds the value of the property with the given name as a header with the given alias of the groups produced by applying this Condition
     * @param name The name of the property
     * @param alias The alias used as name in the group header
     * @return This StringGroupCondition
     */
	public StringGroupCondition head(String name, String alias) {
		additionalProperties.put(name, alias);
		return this;
	}

	protected String getAdditionalPropertyValue(String name, Object item) {
        return asNotNullString(getPropertyValue(item, additionalProperties.get(name)));
	}
}
