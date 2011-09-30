// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.hamcrest.*;

import java.io.*;
import java.util.*;

import ch.lambdaj.*;
import ch.lambdaj.function.convert.*;

/**
 * An Iterator that extends the Iterator interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaIterator<T> extends AbstractLambdaCollection<T> implements Iterator<T>, Serializable {

	LambdaIterator(Iterator<? extends T> inner) {
        super(inner);
    }

    /**
     * Retains all the objects in this iteraor that match the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to retain this iterator
     * @return A sublist of this iterator containing all the objects that match the given hamcrest Matcher
     */
    public LambdaIterator<T> retain(Matcher<?> matcher) {
        return new LambdaIterator<T>((Iterator<T>)Lambda.selectIterator(innerIterator, matcher));
    }

    /**
     * Converts all the object in this iterator using the given {@link Converter}.
     * @param converter The converter that specifies how each object in the iterator must be converted
     * @return A LambdaIterator containing all the objects in this iterator converted using the given {@link Converter}
     */
    public <V> LambdaIterator<V> convert(Converter<T, V> converter) {
        return new LambdaIterator<V>(Lambda.convertIterator(innerIterator, converter));
    }

    /**
	 * Converts all the object in this iterator extracting the property defined by the given argument.
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A LambdaIterable containing the argument's value extracted from the object in this iterator
	 */
    public <V> LambdaIterator<V> extract(V argument) {
        return new LambdaIterator<V>(Lambda.extractIterator(innerIterator, argument));
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Iterator interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
	public boolean hasNext() {
		return innerIterator.hasNext();
	}

    /**
     * {@inheritDoc}
     */
	public T next() {
		return innerIterator.next();
	}

    /**
     * {@inheritDoc}
     */
	public void remove() {
		innerIterator.remove();
	}
}