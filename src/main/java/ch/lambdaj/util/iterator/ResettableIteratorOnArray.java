// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * A ResettabkeIterator that iterates over a wrapped arrey
 * @author Mario Fusco
 */
public class ResettableIteratorOnArray<T> extends ResettableIterator<T> {

    private final T[] array;
    private int counter = 0;

    /**
     * Creates a ResettableIterator that wraps the given array
     * @param array The array to be wrapped
     */
    public ResettableIteratorOnArray(T[] array) {
        this.array = array;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        return counter < array.length;
    }

    /**
     * {@inheritDoc}
     */
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        return array[counter++];
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        counter = 0;
    }
}