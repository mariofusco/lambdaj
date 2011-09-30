// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.compare;

import java.util.Comparator;
import java.io.*;

import static ch.lambdaj.function.compare.ComparatorUtil.nullSafeCompare;
import static ch.lambdaj.util.IntrospectionUtil.getPropertyValue;

/**
 * Compares two objects by comparing the values of one of their property.
 * @author Mario Fusco
 */
public class PropertyComparator<T> implements Comparator<T>, Serializable {

	private final String propertyName;

    /**
     * Creates a comparator that compares two objects by comparing the values of one of their property
     * @param propertyName The name of the property to be compared
     */
	public PropertyComparator(String propertyName) {
		this.propertyName = propertyName;
	}

    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public int compare(T o1, T o2) {
        return nullSafeCompare(getPropertyValue(o1, propertyName), getPropertyValue(o2, propertyName));
	}
}
