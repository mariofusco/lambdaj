// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.util.*;

/**
 * Defines an aggregation operation, i.e. how to aggregate the items of a collection.
 * @author Mario Fusco
 */
public interface Aggregator<T> {

    /**
     * Defines how this operation aggregates a list of objects
     * @param iterator The objects to be aggregated
     * @return The aggregation of the objects
     */
    T aggregate(Iterator<? extends T> iterator);
}
