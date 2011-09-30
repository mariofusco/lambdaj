// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import static java.util.Arrays.asList;

import org.hamcrest.*;
import ch.lambdaj.function.convert.*;

/**
 * A collection that extends the collection interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaCollection<T> extends LambdaIterable<T> implements Collection<T>, Cloneable, Serializable {

    LambdaCollection(Collection<? extends T> inner) {
        super(inner);
    }

    private Collection<T> innerCollection() {
        return (Collection<T>) innerIterable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> retain(Matcher<?> matcher) {
        doRetain(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> remove(Matcher<?> matcher) {
        doRemove(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> sort(Object argument) {
        doSort(argument);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> convert(Converter<T, V> converter) {
        return new LambdaCollection<V>(doConvert(converter));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> extract(V argument) {
        return new LambdaCollection<V>(doExtract(argument));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> replace(Matcher<?> matcher, T replacer) {
        doReplace(matcher, replacer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> distinct(Object argument) {
        doDistinct(argument);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaCollection<V> project(Class<V> targetClass, Object... arguments) {
        return new LambdaCollection<V>(doProject(targetClass, arguments));
    }

    /**
     * Appends the given list of items at the end of this Collection
     * @param list The List of item to be appended
     * @return This Collection with the given list of items added at its end
     */
    public LambdaCollection<T> append(T... list) {
        addAll(asList(list));
        return this;
    }

    /**
     * Returns an array containing all of the items in this collection.
     * @param clazz The class of the items of the array to be created
     * @return An array containing all of the elements in this collection
     */
    public T[] toArray(Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, innerCollection().size());
        int i = 0;
        for (T item : innerCollection()) { array[i++] = item; }
        return array;
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Collection interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public boolean add(T e) {
        return innerCollection().add(e);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(Collection<? extends T> c) {
        return innerCollection().addAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        innerCollection().clear();
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(Object o) {
        return innerCollection().contains(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsAll(Collection<?> c) {
        return innerCollection().containsAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return innerCollection().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public boolean remove(Object o) {
        return innerCollection().remove(o);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeAll(Collection<?> c) {
        return innerCollection().removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public boolean retainAll(Collection<?> c) {
        return innerCollection().retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return innerCollection().size();
    }

    /**
     * {@inheritDoc}
     */
    public Object[] toArray() {
        return innerCollection().toArray();
    }

    /**
     * {@inheritDoc}
     */
    public <T> T[] toArray(T[] a) {
        return innerCollection().toArray(a);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaCollection<T> clone() {
        return new LambdaCollection<T>((Collection<T>)innerClone(new ArrayList<T>()));
    }

}
