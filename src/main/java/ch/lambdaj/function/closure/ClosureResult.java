// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

/**
 * The result returned by a closure defined through the delayed syntax
 * @author Mario Fusco
 */
public interface ClosureResult<T> {

    /**
     * The result returned by the delayed closure invocation
     * @return The closure invocation result
     */
    T get();
}
