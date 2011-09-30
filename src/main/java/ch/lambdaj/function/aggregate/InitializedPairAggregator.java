// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

/**
 * A PairAggregator that is initialized to the value of its idempotent item.
 * @author Mario Fusco
 */
public abstract class InitializedPairAggregator<T> extends PairAggregator<T> {

    private final T firstItem;

    /**
     * Creates a PairAggregator that is initialized to the value of its idempotent item
     * @param firstItem the value with which this PairAggregator is initialized
     */
    public InitializedPairAggregator(T firstItem) {
        this.firstItem = firstItem;
    }

    /**
     * {@inheritDoc}
     */
    public final T emptyItem() {
        return firstItem;
    }
}
