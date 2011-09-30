// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.util.*;
import static java.util.Arrays.asList;

/**
 * This class consists exclusively of static methods that allow to create LambdaCollections.
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public final class LambdaCollections {
    
    private LambdaCollections() { }

    /**
     * Creates a LambdaIterator that wraps the given Iterator
     * @param iterator The Iterator to be wrapped
     * @return The LambdaIterator that wraps the given Iterator
     */
    public static <T> LambdaIterator<T> with(Iterator<? extends T> iterator) {
        return new LambdaIterator<T>(iterator);
    }

    /**
     * Creates a LambdaList that wraps the given List
     * @param list The List to be wrapped
     * @return The LambdaList that wraps the given List
     */
    public static <T> LambdaList<T> with(List<? extends T> list) {
        return new LambdaList<T>(list);
    }

    /**
     * Creates a LambdaList that wraps the given array
     * @param array The array to be wrapped
     * @return The LambdaList that wraps the given array
     */
	public static <T> LambdaList<T> with(T... array) {
		return new LambdaList<T>(new ArrayList<T>(asList(array)));
	}

    /**
     * Creates a LambdaCollection that wraps the given Collection
     * @param collection The collection to be wrapped
     * @return The LambdaCollection that wraps the given Collection
     */
	public static <T> LambdaCollection<T> with(Collection<? extends T> collection) {
		return new LambdaCollection<T>(collection);
	}

    /**
     * Creates a LambdaIterable that wraps the given Iterable
     * @param iterable The Iterable to be wrapped
     * @return The LambdaIterable that wraps the given Iterable
     */
	public static <T> LambdaIterable<T> with(Iterable<? extends T> iterable) {
		return new LambdaIterable<T>(iterable);
	}

    /**
     * Creates a LambdaMap that wraps the given Map
     * @param map The Map to be wrapped
     * @return The LambdaMap that wraps the given Map
     */
	public static <K, V> LambdaMap<K, V> with(Map<? extends K, ? extends V> map) {
		return new LambdaMap<K, V>(map);
	}

    /**
     * Creates a LambdaSet that wraps the given Set
     * @param set The Set to be wrapped
     * @return The LambdaSet that wraps the given Set
     */
	public static <T> LambdaSet<T> with(Set<? extends T> set) {
		return new LambdaSet<T>(set);
	}
}
