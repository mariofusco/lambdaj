// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.compare;

import java.io.*;
import java.util.*;

/**
 * Some utilities methods to compare two objects.
 * @author Mario Fusco
 */
public final class ComparatorUtil {

    private ComparatorUtil() { }

    /**
     * Compares 2 objects in a null safe way
     * @param o1 The first object to be compared
     * @param o2 The second object to be compared
     * @return A positive number if the first object is bigger than the second, a negative if is lesser or zero if they are equal
     */
    public static int nullSafeCompare(Object o1, Object o2) {
        return nullSafeCompare(DEFAULT_ARGUMENT_COMPARATOR, o1, o2);
    }

    static int nullSafeCompare(Comparator<Object> comparator, Object o1, Object o2) {
        if (o1 == null) return o2 == null ? 0 : 1;
        return o2 == null ? -1 : comparator.compare(o1, o2);
    }

    public static Comparator<Object> getStandardComparator(int option) {
        switch(option) {
            case 0:
                return DEFAULT_ARGUMENT_COMPARATOR;
            case 1:
                return DESCENDING_ARGUMENT_COMPARATOR;
            case 2:
                return IGNORE_CASE_ARGUMENT_COMPARATOR;
            case 3:
                return DESCENDING_IGNORE_CASE_ARGUMENT_COMPARATOR;
        }
        throw new RuntimeException("Unknown comparator option: " + option);
    }

    static final Comparator<Object> DEFAULT_ARGUMENT_COMPARATOR = new DefaultArgumentComparator();
	static class DefaultArgumentComparator implements Comparator<Object>, Serializable {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
		public int compare(Object val1, Object val2) {
            if (val1 == null) return val2 == null ? 0 : 1;
			return val2 == null ? -1 : ((Comparable)val1).compareTo(val2);
		}
	}

    static final Comparator<Object> DESCENDING_ARGUMENT_COMPARATOR = new DescendingArgumentComparator();
    static class DescendingArgumentComparator extends DefaultArgumentComparator {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public int compare(Object val1, Object val2) {
            int asc = super.compare(val1, val2);
            return -asc;
        }
    }

    static final Comparator<Object> IGNORE_CASE_ARGUMENT_COMPARATOR = new IgnoreCaseArgumentComparator();
    static class IgnoreCaseArgumentComparator implements Comparator<Object>, Serializable {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public int compare(Object val1, Object val2) {
            if (val1 == null) return val2 == null ? 0 : 1;
            return val2 == null ? -1 : ((String)val1).compareToIgnoreCase((String)val2);
        }
    }

    static final Comparator<Object> DESCENDING_IGNORE_CASE_ARGUMENT_COMPARATOR = new DescendingIgnoreCaseArgumentComparator();
    static class DescendingIgnoreCaseArgumentComparator extends IgnoreCaseArgumentComparator {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        public int compare(Object val1, Object val2) {
            return -super.compare(val1, val2);
        }
    }
}
