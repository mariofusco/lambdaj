// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.compare;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.function.compare.ComparatorUtil.*;

import java.util.*;
import java.io.*;

import ch.lambdaj.function.argument.*;

/**
 * Compares two objects by comparing the values returned by an Argument call on them.
 * @author Mario Fusco
 */
public class ArgumentComparator<T, A> implements Comparator<T>, Serializable {

	private final Argument<A> argument;
	private final Comparator<Object> comparator;
	
    /**
     * Creates a comparator that compares two objects by comparing the values returned by an Argument call on them
     * @param argument The argument identifying the property to be compared
     */
	public ArgumentComparator(A argument) {
		this(actualArgument(argument));
	}

    /**
     * Creates a comparator that compares two objects by comparing the values returned by an Argument call on them
     * @param argument The argument identifying the property to be compared
     */
	public ArgumentComparator(Argument<A> argument) {
        this.argument = argument;
        this.comparator = DEFAULT_ARGUMENT_COMPARATOR;
	}
	
    /**
     * Creates a comparator that compares two objects by comparing the values returned by an Argument call on them
     * @param argument The argument identifying the property to be compared
     * @param comparator The comparator used to compare the values of the arguments
     */
	public ArgumentComparator(A argument, Comparator<A> comparator) {
		this(actualArgument(argument), comparator);
	}
	
    /**
     * Creates a comparator that compares two objects by comparing the values returned by the invocations on the given Argument
     * @param argument The argument identifying the property to be compared
     * @param comparator The comparator used to compare the values of the arguments
     */
    @SuppressWarnings("unchecked")
	public ArgumentComparator(Argument<A> argument, Comparator<A> comparator) {
		this.argument = argument;
		this.comparator = (Comparator<Object>)comparator;
	}
	
    /**
     * {@inheritDoc}
     */
	public int compare(T o1, T o2) {
		return nullSafeCompare(comparator, argument.evaluate(o1), argument.evaluate(o2));
	}
}