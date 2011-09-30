// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.util.*;

/**
 * An aggregator that uses the definition of how to aggregate a pair of items
 * and of the empty (idempotent respect to this aggregation) item.
 * @author Mario Fusco
 */
public abstract class PairAggregator<T> implements Aggregator<T> {

    /**
     * Aggregates the objects in the given iterable by aggregating the i-th item with the aggreagation of all the
     * former i-1 items. The first item is aggregated with what as been defined as the empty (idempotent) item.
     * @param iterator The objects to be aggregated
     * @return The aggregation of the objects
     */
    public T aggregate(Iterator<? extends T> iterator) {
        T result = emptyItem();
        if (iterator != null) while (iterator.hasNext()) { result = aggregate(result, iterator.next()); }
        return result;
    }

    /**
     * Returns the idempotent item for this aggregation operation
     */
    protected abstract T emptyItem();

    /**
     * Defines how this operation aggregates 2 different objects
     * @param first The first object to be aggregated
     * @param second The second object to be aggregated
     * @return The aggregation of first and second objects
     */
    protected abstract T aggregate(T first, T second);
}
