// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * A class of utility static method used to build the different kinds of Iterators used in lambdaj
 * @author Mario Fusco
 */
public final class IteratorFactory {

    private IteratorFactory() { }

    /**
     * Discovers the generic type of the given Iterable based on the type of its first item (if any
     * @param iterable The Iterable to be analyzed
     * @return The Class of the first item of this iterable (if any)
     */
    public static Class<?> discoverGenericType(Iterable<?> iterable) {
        return discoverGenericType(iterable.iterator());
    }

    /**
     * Discovers the generic type of the given Iterator based on the type of its first item (if any)
     * @param iterator The Iterator to be analyzed
     * @return The Class of the first item of this iterator (if any)
     */
    public static Class<?> discoverGenericType(Iterator<?> iterator) {
        if (!iterator.hasNext())
            throw new IllegalArgumentException("Unable to introspect on an empty iterator. Use the overloaded method accepting a class instead");
        Object next = iterator.next();
        return next != null ? next.getClass() : Object.class;
    }

    /**
     * Tries to convert a generic object in an Iterator.
     * This method works with Iterators, Arrays, Iterables and Maps and
     * in this last case an Iterator over the Map's values is returned.
     * If the object is null returns an Iterator over an empty collection.
     * If none of the above applies throws an IllegalArgumentException.
     * @param object The object to be converted
     * @return The Iterator resulting from the object conversion
     * @throws IllegalArgumentException if the given object is neither an Iterator, Array, Iterable or Map.
     */
    public static Iterator<?> asIterator(Object object) {
        if (object == null) return new ArrayList().iterator();
        if (object instanceof Iterable) return ((Iterable<?>)object).iterator();
        if (object instanceof Iterator) return (Iterator<?>)object;
        if (object.getClass().isArray()) return new ResettableIteratorOnArray<Object>((Object[])object);
        if (object instanceof Map) return ((Map<?,?>)object).values().iterator();
        throw new IllegalArgumentException("Cannot convert " + object + " to an iterator");
    }

    /**
     * Tries to convert a generic object in a ResettableIterator.
     * This method works with Iterators, Arrays, Iterables and Maps and
     * in this last case a ResettableIterator over the Map's values is returned.
     * If the object is null returns aa ResettableIterator over an empty collection.
     * If none of the above applies throws an IllegalArgumentException.
     * @param object The object to be converted
     * @return The ResettableIterator resulting from the object conversion
     * @throws IllegalArgumentException if the given object is neither an Iterator, Array, Iterable or Map.
     */
    public static ResettableIterator<?> asResettableIterator(Object object) {
        if (object == null) return new ResettableIteratorOnIterable(new ArrayList());
        if (object instanceof Iterable) return new ResettableIteratorOnIterable((Iterable<?>)object);
        if (object instanceof Iterator) return new ResettableIteratorOnIterator((Iterator<?>)object);
        if (object.getClass().isArray()) return new ResettableIteratorOnArray<Object>((Object[])object);
        if (object instanceof Map) return new ResettableIteratorOnIterable(((Map<?,?>)object).values());
        throw new IllegalArgumentException("Cannot convert " + object + " to an iterator");
    }

    /**
     * Flattens the given iterable by recursively descending through its nested Collections
     * and create a flat List of all of the leaves.
     * This method also works with Maps (by collecting their values) and arrays.
     * @param iterable The iterable to be flattened
     * @return The flattened iterable
     */
    public static <T> List<T> flattenIterator(Object iterable) {
        List<Object> flattened = new LinkedList<Object>();
        try {
            flattened.addAll(flattenIterator(asIterator(iterable)));
        } catch (IllegalArgumentException iae) {
            flattened.add(iterable);
        }
        return (List<T>)flattened;
    }

    private static <Object> List<Object> flattenIterator(Iterator iterator) {
        List<Object> flattened = new LinkedList<Object>();
        while (iterator.hasNext()) { flattened.addAll((List<Object>)flattenIterator(iterator.next())); }
        return flattened;
    }
}
