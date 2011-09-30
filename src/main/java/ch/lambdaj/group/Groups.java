// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import java.util.*;

/**
 * This class consists exclusively of static methods that allow to use the lambdaj grouping feature.
 * @author Mario Fusco
 */
public final class Groups {
	
	private Groups() { }

    /**
     * Creates a GroupCondition that allows to group items based on the value they have on a given argument
     * @param argument The argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method on which the items have to be grouped
     * @return A GroupCondition that can be used to group items through the {@link Groups#group(Iterable, GroupCondition[])} method
     */
    public static <T> ArgumentGroupCondition<T> by(T argument) {
		return new ArgumentGroupCondition<T>(argument);
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the given grouping conditions
     * @param iterable The items to be grouped
     * @param conditions The conditions that define how the items have to be grouped
     * @return The items grouped by the given conditions
     */
	public static <T> Group<T> group(Iterable<T> iterable, Collection<? extends GroupCondition<?>> conditions) {
		return group(iterable, conditions.toArray(new GroupCondition<?>[conditions.size()]));
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the values they have on the named JavaBean proprties
     * @param iterable The items to be grouped
     * @param groupers The names of the properties on which the items have to be grouped
     * @return The items grouped on the values of their JavaBean properties
     */
	public static <T> Group<T> group(Iterable<T> iterable, String... groupers) {
		GroupCondition[] conditions = new GroupCondition[groupers.length];
		int i = 0;
		for (String grouper : groupers) { conditions[i++] = new StringGroupCondition(grouper); }
		return group(iterable, conditions);
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the given grouping conditions
     * @param iterable The items to be grouped
     * @param conditions The conditions that define how the items have to be grouped
     * @return The items grouped by the given conditions
     */
	public static <T> Group<T> group(Iterable<T> iterable, GroupCondition<?>... conditions) {
		GroupCondition<?> condition = conditions[0];
		GroupImpl<T> group = new GroupImpl<T>(condition);
		for (T item : iterable)	{ group.addItem(item); }

		if (conditions.length > 1) {
			GroupCondition<?>[] newConditions = new GroupCondition<?>[conditions.length - 1];
			System.arraycopy(conditions, 1, newConditions, 0, newConditions.length);
			for (GroupItem<T> groupItem : group) { groupItem.setChildren((GroupImpl<T>)group(groupItem, newConditions)); }
		}

        condition.sortGroup(group);
		return group;
	}
}
