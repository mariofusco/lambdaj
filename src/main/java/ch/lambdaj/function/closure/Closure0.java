// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

/**
 * A closure with no free variables
 * @author Mario Fusco
 */
public class Closure0 extends AbstractClosure {

    /**
     * Invoke this closure
     * @return The result of the closure invocation
     */
	public Object apply() {
		return closeOne();
	}

    /**
     * Defines the method invoked by this closure.
     * @param closedObject The object on which the closure has to be invoked. It can be a fixed object or a Class.
     *                     In this last case, if the method is not static, it is treated as it was an
     *                     unbound argument defined through the {@link ch.lambdaj.Lambda#var(Class)} method
     * @param methodName The name of the method invoked by this closure or {@link AbstractClosure#CONSTRUCTOR}
      *                   if you want to call a constructor
      * @param args The arguments used to invoke this closure. They can be a mixed of fixed value and
     *             unbound one defined through the {@link ch.lambdaj.Lambda#var(Class)} method
     * @return The closure itself
     */
    @Override
    public Closure0 of(Object closedObject, String methodName, Object ... args) {
        return (Closure0)super.of(closedObject, methodName, args);
    }
}
