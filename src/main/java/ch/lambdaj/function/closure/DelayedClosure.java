// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.createClosure;

/**
 * Extends this class in order to define a closure that can be used with a nicer and more readable syntax as it follows:
 * <pre>
 *  withTransaction(); {
 *      of(this).doSomething();
 *  }
 * </pre>
 * To achieve this result the withTransaction() should be defined as:
 * <pre>
 *  public void withTransaction() {
 *      delayedClosure(new DelayedClosure&lt;Void&gt;() {
 *          public Void doWithClosure(Closure closure) {
 *              beginTransaction();
 *              closure.apply();
 *              commit();
 *              return null;
 *          }
 *      });
 *  }
 * </pre>
 * It is also possible to get a result from the method that uses this kind of closure by reading it from the getClosureResult() method
 * @author Guillaume Bort
 * @author Mario Fusco
 */
public abstract class DelayedClosure<T> {

    private final Closure closure;
    private final ClosureResult<T> closureResult;
    private T result;

    private static final ThreadLocal<DelayedClosure<?>> CURRENT_DELAYED = new ThreadLocal<DelayedClosure<?>>();

    /**
     * Creates a DelayedClosure
     */
    public DelayedClosure() {
        closure = createClosure();
        closureResult = new ClosureResult<T>() { public T get() { return result; }};
        CURRENT_DELAYED.set(this);
    }

    /**
     * Override this method to define how the closure has to be used during its delayed invocation
     * @param closure The closure used inside this delayed invocation
     * @return The result of the closure invocation
     */
    public abstract T doWithClosure(Closure closure);

    /**
     * Returns the result of the invocation of the method that uses this closure 
     * @return A ClosureResult containing the result of the invocation of the method that uses this closure
     */
    public final ClosureResult<T> getClosureResult() {
        return closureResult;
    }

    static void call() {
        DelayedClosure delayedClosure = CURRENT_DELAYED.get();
        CURRENT_DELAYED.set(null);
        if (delayedClosure != null) delayedClosure.execute();
    }

    private void execute() {
        result = doWithClosure(closure);
    }

    /**
     * Cleans delayed closures currently bound to actual thread's thread local.
     */
    public static void cleanupDelayed() {
        CURRENT_DELAYED.remove();
    }

}
