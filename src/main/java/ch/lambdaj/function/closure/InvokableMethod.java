// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.lang.reflect.*;

/**
 * An Invokable that invokes a method
 * @author Mario Fusco
 */
class InvokableMethod implements Invokable {

    private final Method method;

    /**
     * Creates A Invokable that invokes a method
     * @param method the method to be invoked
     */
    public InvokableMethod(Method method) {
        this.method = method;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object obj, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new WrongClosureInvocationException("Error invoking " + method + " on " + obj, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }
}
