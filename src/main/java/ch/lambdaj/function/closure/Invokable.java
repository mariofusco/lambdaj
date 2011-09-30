// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

/**
 * @author Mario Fusco
 */
interface Invokable {

    /**
     * Inkvokes the given object with this Invokable using the given set of arguments
     * @param obj The object on which this Invokable will be invoked
     * @param args The argument used to invoke this Invokable
     * @return The result of the invocation
     */
    Object invoke(Object obj, Object... args);

    /**
     * Returns true if this invokable is static
     * @return True if this invokable is static false otherwise
     */
    boolean isStatic();
}
