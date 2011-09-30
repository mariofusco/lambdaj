// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.hamcrest.*;

import java.io.*;
import java.util.*;

/**
 * A Set that extends the Set interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaSet<T> extends LambdaCollection<T> implements Set<T>, Cloneable, Serializable {

	LambdaSet(Set<? extends T> inner) {
        super(inner);
	}

    /**
     * {@inheritDoc}
     */
    public LambdaSet<T> retain(Matcher<?> matcher) {
        doRetain(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public LambdaSet<T> remove(Matcher<?> matcher) {
        doRemove(matcher);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LambdaSet<T> clone() {
        return clone(new HashSet<T>());
    }

    /**
     * Returns a shallow copy of this LambdaSet instance. (The elements themselves are not copied.)
     * @param emptyList The empty set to be used as wrapped set of this LambdaSet if the current one is not Cloneable
     * @return A clone of this LambdaSet instance
     */
    public LambdaSet<T> clone(Set<? extends T> emptyList) {
        return new LambdaSet<T>((Set<T>)innerClone(emptyList));
    }
}
