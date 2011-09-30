// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * A ResettabkeIterator that iterates over a wrapped Iterator
 * @author Mario Fusco
 */
public class ResettableIteratorOnIterator<T> extends ResettableIterator<T> {

    private final Iterator<T> iterator;

    private final List<T> innerIterable = new LinkedList<T>();
    private Iterator<T> innerIterator;

    private final List<T> cache = new LinkedList<T>();

    /**
     * Creates a ResettableIterator that wraps the given Iterator
     * @param iterator The Iterator to be wrapped
     */
    public ResettableIteratorOnIterator(Iterator<T> iterator) {
        this.iterator = iterator;
        innerIterator = innerIterable.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        innerIterable.addAll(cache);
        cache.clear();
        innerIterator = innerIterable.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        return iterator.hasNext() || innerIterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public T next() {
        if (innerIterator.hasNext()) return innerIterator.next();
        if (iterator.hasNext()) {
            T next = iterator.next();
            cache.add(next);
            return next;
        }
        throw new NoSuchElementException();
    }
}