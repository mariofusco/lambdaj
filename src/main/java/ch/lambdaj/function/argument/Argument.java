// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import ch.lambdaj.Lambda;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An Argument represents a statically defined sequence of method invocations on a given Class.
 * @author Mario Fusco
 */
public class Argument<T> {

	private final InvocationSequence invocationSequence;

    private final boolean isJittable;

    private Invoker jittedInvoker;

    private AtomicInteger invocationCounter = new AtomicInteger(0);

    private static final Map<String, Invoker> invokerCache = new HashMap<String, Invoker>();

    Argument(InvocationSequence invocationSequence) {
		this.invocationSequence = invocationSequence;
        isJittable = Lambda.jitThreshold >= 0 && invocationSequence.isJittable();
        if (isJittable) jittedInvoker = invokerCache.get(invocationSequence.toString());
	}
	
	/**
	 * The JavaBean compatible names of the properties defined by the invocations sequence of this Argument.
	 * For example on an Argument defined as <code>on(Person.class).getBestFriend().isMale()</code> it returns "bestFriend.male"
     * @return The names of the properties defined by the invocations sequence of this Argument
	 */
	public String getInkvokedPropertyName() {
		return invocationSequence.getInkvokedPropertyName();
	}

	/**
	 * Evaluates this Argument on the given object
	 * @param object The Object on which this Argument should be evaluated. It must be compatible with the Argument's root class.
	 * @return The value of this Argument for the given Object
	 */
	@SuppressWarnings("unchecked")
	public T evaluate(Object object) {
        if (jittedInvoker != null) return (T)jittedInvoker.invokeOn(object);
        if (!isJittable) return (T)invocationSequence.invokeOn(object);

        if (invocationCounter.getAndIncrement() == Lambda.jitThreshold) {
            jittedInvoker = new InvokerJitter(object, invocationSequence).jitInvoker();
            invokerCache.put(invocationSequence.toString(), jittedInvoker);
            return (T)jittedInvoker.invokeOn(object);
        }

        return (T)invocationSequence.invokeOn(object);
	}
	
	/**
	 * Returns the root class from which the sequence of method invocation defined by this argument starts
	 */
	public Class<?> getRootArgumentClass() {
		return invocationSequence.getRootInvokedClass();
	}
	
	/**
	 * Returns the type returned by the last method of the invocations sequence represented by this Argument.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getReturnType() {
		return (Class<T>)invocationSequence.getReturnType();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public String toString() {
		return invocationSequence.toString();
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public boolean equals(Object object) {
		return object instanceof Argument<?> && invocationSequence.equals(((Argument<?>)object).invocationSequence);
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public int hashCode() {
		return invocationSequence.hashCode();
	}
}
