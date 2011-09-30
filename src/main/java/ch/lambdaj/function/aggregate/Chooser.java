// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * An abstract class that define the behaviour af an Aggregator which aggregates two items
 * of a collection just by choosing one of them.
 * @author Mario Fusco
 */
public abstract class Chooser<T> extends PairAggregator<T> {

    /**
     * Chooses on the two items returning the non-null one if one of them is null or by applying
     * the strategy defined by the {@link Chooser#choose(Object, Object)} method if both of them are not null
     * @param first The first item on which make the choice
     * @param second The second item on which make the choice
     * @return The choosen item
     */
	public T aggregate(T first, T second) {
		if (first == null) return second;
		if (second == null) return first;
		return choose(first, second);
	}

    /**
     * Chooses between two non null items.
     * @param first The first item on which make the choice
     * @param second The second item on which make the choice
     * @return The choosen item
     */
	protected abstract T choose(T first, T second);

    /**
     * Just returns null which is the idempotent item for a choosing aggreagation
     * @return null
     */
	public T emptyItem() {
		return null;
	}
}
