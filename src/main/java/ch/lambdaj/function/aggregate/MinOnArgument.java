// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * A chooser of the item having the minimum value on an argument operation invoked on it.
 * @author Mario Fusco
 */
public class MinOnArgument<T, A> extends ChooserOnArgument<T, A> {

    /**
     * Creates a chooser that uses the value extracted by applying the given argument
     * @param argument The argument used to extract values from the items between which to find the minimum
     */
	public MinOnArgument(A argument) {
		super(argument);
	}

    /**
     * Finds the item having the minimum value on the given argument
     * @param first The first item on which make the choice
     * @param firstArgument The value of the argument evaluated on the first item
     * @param second The second item on which make the choice
     * @param secondArgument The value of the argument evaluated on the second item
     * @return The item having the minimum value on the given argument
     */
	@SuppressWarnings("unchecked")
	@Override
	protected T chooseOnArgument(T first, A firstArgument, T second, A secondArgument) {
		return ((Comparable)firstArgument).compareTo(secondArgument) < 0 ? first : second;
	}
}
