// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with a single free variable
 * @author Mario Fusco
 */
public class Closure1<A> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given variable to it.
      * @param var The variable used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A var) {
		return closeOne(var);
	}
	
    /**
     * Invokes this closure once for each passed variable.
     * @param vars The set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(A... vars) {
		return closeAll(vars);
	}
	
    /**
     * Invokes this closure once for each item in the given iterable.
     * @param vars The variables used to invoke this closure once for each of its item
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(Iterable<? extends A> vars) {
		return closeAll(vars);
	}
	
    /**
     * Curry this closure by fixing its only free variable to a given value.
     * @param curry The value to which the free variable should be curry
     * @return A Closure having no free variable
     */
	public Closure0 curry(A curry) {
		return curry(new Closure0(), curry, 1);
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
    public Closure1<A> of(Object closedObject, String methodName, Object ... args) {
        return (Closure1<A>)super.of(closedObject, methodName, args);
    }
}
