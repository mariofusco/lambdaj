// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * A chooser of the minimum value between two comparable
 * @author Mario Fusco
 */
public class Min<T> extends Chooser<Comparable<T>> {

    /**
     * Chooses the minimum value between two Comparable
     * @param first The first item between which find the minimum
     * @param second The second item between which find the minimum
     * @return The smallest between the two items
     */
	@SuppressWarnings("unchecked")
	@Override
	protected Comparable<T> choose(Comparable<T> first, Comparable<T> second) {
		return first.compareTo((T) second) < 0 ? first : second;
	}
}
