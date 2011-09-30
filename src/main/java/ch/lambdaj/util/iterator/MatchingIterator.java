// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util.iterator;

import org.hamcrest.*;

import java.util.*;

/**
 * Wraps an iterator filtering its items with the given hamcrest Matcher
 * @author Mario Fusco
 * @author Andrea Polci
 */
public class MatchingIterator<T> implements Iterator<T> {

	private final Iterator<T> iterator;
	private final Matcher<?> matcher;

	private T nextElement;
    private boolean nextAvailable = false;

    /**
     * Creates an Iterator that wraps another iterator filtering its items with the given hamcrest Matcher
     * @param iterator The iterator to be wrapped and filtered
     * @param matcher The matcher used to filter the wrapped iterator
     */
    public MatchingIterator(Iterator<T> iterator, Matcher<?> matcher) {
		this.iterator = iterator;
		this.matcher = matcher;
	}

	private void searchNext() {
		nextElement = null;
		while (iterator.hasNext() && nextElement == null) {
			T n = iterator.next();
			if (matcher.matches(n)) nextElement = n;
		}
        nextAvailable = true;
	}

    /**
     * {@inheritDoc}
     */
	public boolean hasNext() {
        if (!nextAvailable) searchNext();
		return nextElement != null;
	}

    /**
     * {@inheritDoc}
     */
	public T next() {
		if (!hasNext()) throw new NoSuchElementException();
		T n = nextElement;
		nextAvailable = false;
		return n;
	}

    /**
     * {@inheritDoc}
     */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}