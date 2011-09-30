// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.hamcrest.*;

import java.io.*;
import java.util.*;

import ch.lambdaj.function.convert.*;

/**
 * A List that extends the List interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaList<T> extends LambdaCollection<T> implements List<T>, Cloneable, Serializable {

	LambdaList(List<? extends T> inner) {
        super(inner);
	}

    private List<T> innerList() {
        return (List<T>) innerIterable;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> retain(Matcher<?> matcher) {
        doRetain(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> remove(Matcher<?> matcher) {
        doRemove(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> sort(Object argument) {
        doSort(argument);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public <V> LambdaList<V> convert(Converter<T, V> converter) {
        return new LambdaList<V>(doConvert(converter));
    }

    /**
     * {@inheritDoc}
     */
    public <V> LambdaList<V> extract(V argument) {
        return new LambdaList<V>(doExtract(argument));
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> replace(Matcher<?> matcher, T replacer) {
        doReplace(matcher, replacer);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> distinct(Object argument) {
        doDistinct(argument);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> LambdaList<V> project(Class<V> targetClass, Object... arguments) {
        return new LambdaList<V>(doProject(targetClass, arguments));
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// List interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
    public void add(int index, T element) {
        innerList().add(index, element);
    }

    /**
     * {@inheritDoc}
     */
    public boolean addAll(int index, Collection<? extends T> c) {
        return innerList().addAll(index, c);
    }

    /**
     * {@inheritDoc}
     */
    public T get(int index) {
        return innerList().get(index);
    }

    /**
     * {@inheritDoc}
     */
    public int indexOf(Object o) {
        return innerList().indexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    public int lastIndexOf(Object o) {
        return innerList().lastIndexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<T> listIterator() {
        return innerList().listIterator();
    }

    /**
     * {@inheritDoc}
     */
    public ListIterator<T> listIterator(int index) {
        return innerList().listIterator(index);
    }

    /**
     * {@inheritDoc}
     */
    public T remove(int index) {
        return innerList().remove(index);
    }

    /**
     * {@inheritDoc}
     */
    public T set(int index, T element) {
        return innerList().set(index, element);
    }

    /**
     * {@inheritDoc}
     */
    public LambdaList<T> subList(int fromIndex, int toIndex) {
        return new LambdaList(innerList().subList(fromIndex, toIndex));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaList<T> clone() {
        return clone(new ArrayList<T>());
    }

    /**
     * Returns a shallow copy of this LambdaList instance. (The elements themselves are not copied.)
     * @param emptyList The empty list to be used as wrapped list of this LambdaList if the current one is not Cloneable
     * @return A clone of this LambdaList instance
     */
    public LambdaList<T> clone(List<? extends T> emptyList) {
        return new LambdaList<T>((List<T>)innerClone(emptyList));
    }
}
