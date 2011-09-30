// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.group;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.function.compare.ComparatorUtil.nullSafeCompare;

import ch.lambdaj.function.argument.*;

import java.util.*;
import java.io.*;

/**
 * A comparator that allows to sort group based on the value assumed on the given argument
 * by the object that is the key for a group itself
 * @author Mario Fusco
 */
class GroupComparator<A extends Comparable<A>> implements Comparator<GroupItem<?>>, Serializable {

    private final Argument<A> argument;

    /**
     * Creates a comparator that compares two groups by comparing the values assumed on the given argument
     * @param argument The argument identifying the property to be compared
     */
    public GroupComparator(A argument) {
        this(actualArgument(argument));
    }

    /**
     * Creates a comparator that compares two groups by comparing the values assumed on the given argument
     * @param argument The argument identifying the property to be compared
     */
    public GroupComparator(Argument<A> argument) {
        this.argument = argument;
    }

    /**
     * {@inheritDoc}
     */
    public int compare(GroupItem<?> group1, GroupItem<?> group2) {
        return nullSafeCompare(argument.evaluate(group1.getGroupKey()), argument.evaluate(group2.getGroupKey()));
    }
}
