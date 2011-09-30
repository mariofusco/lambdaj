// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

/**
 * An Aggregator that chooses between two item based on the value of an argument operation invoked on them.
 * @author Mario Fusco
 */
public abstract class ChooserOnArgument<T, A> extends Chooser<T> {

	private final Argument<A> argument;

    /**
     * Creates a chooser that uses the value extracted by applying the given argument
     * @param argument The argument used to extract values from the items between which to choose
     */
	public ChooserOnArgument(A argument) {
		this.argument = actualArgument(argument);
	}

    /**
     * Chooses between two non null items based on the value of one of their argument
     * @param first The first item on which make the choice
     * @param second The second item on which make the choice
     * @return The choosen item
     */
	@Override
	protected T choose(T first, T second) {
		A firstArgument = argument.evaluate(first);
		if (firstArgument == null) return second;
		A secondArgument = argument.evaluate(second);
		if (secondArgument == null) return first;
		return chooseOnArgument(first, firstArgument, second, secondArgument);
	}

    /**
     * Chooses between two non null items based on the value of one of their argument
     * @param first The first item on which make the choice
     * @param firstArgument The value of the argument evaluated on the first item
     * @param second The second item on which make the choice
     * @param secondArgument The value of the argument evaluated on the second item
     * @return The choosen item
     */
	protected abstract T chooseOnArgument(T first, A firstArgument, T second, A secondArgument);
}
