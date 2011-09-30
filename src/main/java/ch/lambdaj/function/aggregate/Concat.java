// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.util.*;

/**
 * An Aggregator that concats String or more generally the String representation of a given Object
 * @author Mario Fusco
 */
public class Concat implements Aggregator<Object> {

	private final String separator;

    /**
     * Creates an aggregator that concats Strings using the default separator \", \"
     */
	public Concat() {
		this(", ");
	}

    /**
     * Creates an aggregator that concats Strings using the given separator
     * @param separator The string used to separate two concatenated Strings
     */
	public Concat(String separator) {
		this.separator = separator;
	}

    /**
     * Aggregates two object by concatenating their String representation separating them with the choosen separator.
     * It has been implemented a custom implementation for this method instead of using the one provided
     * by the PairAggregator for performance reason, in order to levarege the StringBuilder features.
     * @param iterator The objects of which the String representation should be concatenated
     * @return A String resulting of the objects Stringification separated by the given argument
     */
    public Object aggregate(Iterator<?> iterator) {
        if (iterator == null) return "";
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item == null) continue;
            String itemAsString = item.toString();
            if (!itemAsString.trim().equals("")) {
                sb.append(itemAsString);
                break;
            }
        }
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item == null) continue;
            String itemAsString = item.toString();
            if (!itemAsString.trim().equals("")) sb.append(separator).append(itemAsString);
        }
        return sb.toString();
    }
}
