// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import java.lang.reflect.*;

/**
 * An Invokable that invokes a constructor
 * @author Mario Fusco
 */
class InvokableConstructor implements Invokable {

    private final Constructor<?> constructor;

    /**
     * Creates A Invokable that invokes a constructor
     * @param constructor the constrctor to be invoked
     */
    public InvokableConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    /**
     * {@inheritDoc}
     */
    public Object invoke(Object obj, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new WrongClosureInvocationException("Error invoking constroctor for " + constructor.getDeclaringClass(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isStatic() {
        return true;
    }
}
