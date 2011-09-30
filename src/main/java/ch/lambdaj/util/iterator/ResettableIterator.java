// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import java.util.*;

/**
 * An Iterator that can reset its cursor to its initial position
 * @author Mario Fusco
 */
public abstract class ResettableIterator<T> implements Iterator<T> {

    /**
     * Resets the cursor of this Iterator to its initial position
     */
    public abstract void reset();

    /**
     * {@inheritDoc}
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
