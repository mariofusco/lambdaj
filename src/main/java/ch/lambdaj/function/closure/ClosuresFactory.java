// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.proxy.ProxyUtil.*;

import ch.lambdaj.function.argument.*;
import ch.lambdaj.proxy.*;

/**
 * An utility class of static factory methods that create closures
 * @author Mario Fusco
 */
public final class ClosuresFactory {

    private ClosuresFactory() { }

	private static final ThreadLocal<AbstractClosure> CLOSURES = new ThreadLocal<AbstractClosure>();

    /**
     * Binds an object to the active closure that is the last one created in the current thread. After binding
     * closure in current context is removed from ThreadLocal.
     *
     * @param closed The object that has to be bind to the active closure
     * @param closedClass The actual class of the proxied object
     * @return An instance of the closedClass that is actually a proxy used to register all the invocation on the closed object
     * @throws UndefinedClosureException if closure can not be bound
     */
	public static <T> T bindClosure(Object closed, Class<T> closedClass) {
        if (CLOSURES.get() == null) {
            throw new UndefinedClosureException("Can not bind closure in current context. Have you invoked Lambda.closure()?");
        }

        try {
		return CLOSURES.get().of(closed, closedClass);
        } finally {
            CLOSURES.remove();
        }
	}

    /**
     * Creates a proxy used to register all the invocation on the closed object
     * @param closure The closure to which the invocations on the returned proxy are related
     * @param closedClass The actual class of the proxied object
     * @return An instance of the closedClass that is actually a proxy used to register all the invocation on the closed object
     */
	static <T> T createProxyClosure(AbstractClosure closure, Class<T> closedClass) {
		return createProxy(new ProxyClosure(closure), closedClass, true);
	}

    /**
     * Creates a generic (not typed) closure and binds it to the current thread
     * @return The newly created closure
     */
	public static Closure createClosure() {
		Closure closure = new Closure();
		CLOSURES.set(closure);
		return closure;
	}

    /**
     * Creates a closure with a single free variable and binds it to the current thread
     * @param type1 The type of the free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A> Closure1<A> createClosure(Class<A> type1) {
		Closure1<A> closure = new Closure1<A>();
		CLOSURES.set(closure);
		return closure;
	}

    /**
     * Creates a closure with two free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B> Closure2<A, B> createClosure(Class<A> type1, Class<B> type2) {
		Closure2<A, B> closure = new Closure2<A, B>();
		CLOSURES.set(closure);
		return closure;
	}

    /**
     * Creates a closure with three free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @param type3 The type of the third free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B, C> Closure3<A, B, C> createClosure(Class<A> type1, Class<B> type2, Class<C> type3) {
		Closure3<A, B, C> closure = new Closure3<A, B, C>();
		CLOSURES.set(closure);
		return closure;
	}

    /**
     * Creates a closure with three free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @param type3 The type of the third free variable of the newly created closure
     * @param type4 The type of the fourth free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B, C, D> Closure4<A, B, C, D> createClosure(Class<A> type1, Class<B> type2, Class<C> type3, Class<D> type4) {
		Closure4<A, B, C, D> closure = new Closure4<A, B, C, D>();
		CLOSURES.set(closure);
		return closure;
	}

    /**
     * Defines a free variable of the given Class for the currently active closure
     * @param clazz The Class of the new variable
     * @return A placeholder that represent a free closure variable of the given Class
     */
    public static <T> T createClosureVarPlaceholder(Class<T> clazz) {
		return createClosureArgumentPlaceholder(clazz);
	}

    /**
     * Tests if the given object is actually a placeholder for a free variable of a closure
     * @param object The object to be tested
     * @return true if the given object is actually a placeholder for a free variable of a closure
     */
	static ClosureVarType getClosureVarType(Object object) {
        if (object == null) return ClosuresFactory.ClosureVarType.FIXED;
		if (getClosureVarArgument(object) != null) return ClosuresFactory.ClosureVarType.VAR;
        if (createClosureArgumentPlaceholder(object.getClass()).equals(object)) return ClosuresFactory.ClosureVarType.FINAL_VAR;
        return ClosuresFactory.ClosureVarType.FIXED;
	}

    static <T> Argument<T> getClosureVarArgument(T placeholder) {
        return placeholderToClosureArgument(placeholder);
    }

    enum ClosureVarType {
        VAR, FINAL_VAR, FIXED;

        boolean isClosureVarPlaceholder() {
            return this == VAR || this == FINAL_VAR;
        }
    }

    /**
     * Cleans closures currently bound to actual thread's thread local.
     */
    public static void cleanupClosures() {
       CLOSURES.remove();
    }
}
