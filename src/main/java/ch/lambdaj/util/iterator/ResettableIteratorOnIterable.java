// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * A ResettabkeIterator that iterates over a wrapped Iterable
 * @author Mario Fusco
 */
public class ResettableIteratorOnIterable<T> extends ResettableIterator<T> {

    private final Iterable<T> iterable;

    private Iterator<T> iterator;

    /**
     * Creates a ResettableIterator that wraps the given Iterable
     * @param iterable The Iterable to be wrapped
     */
    public ResettableIteratorOnIterable(Iterable<T> iterable) {
        this.iterable = iterable;
        reset();
    }

    /**
     * {@inheritDoc}
     */
    public final void reset() {
        iterator = iterable.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    public T next() {
        return iterator.next();
    }
}
