// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import java.lang.ref.*;
import java.lang.reflect.*;
import java.util.*;

import ch.lambdaj.util.*;

/**
 * Registers a method invocation
 *
 * @author Mario Fusco
 * @author Frode Carlsen
 */
final class Invocation {

    private final Class<?> invokedClass;
    private final Method invokedMethod;
    private String invokedPropertyName;
    private ParameterReference[] weakArgs;
    private transient int hashCode;
    Invocation previousInvocation;

    Invocation(Class<?> invokedClass, Method invokedMethod, Object[] args) {
        this.invokedClass = invokedClass;
        this.invokedMethod = invokedMethod;
        invokedMethod.setAccessible(true);
        if (args != null && args.length > 0) {
            weakArgs = new ParameterReference[args.length];
            for (int i = 0; i < args.length; i++) {
                weakArgs[i] = invokedMethod.getParameterTypes()[i].isPrimitive() ? new StrongParameterReference(args[i]) : new WeakParameterReference(args[i]);
            }
        }
    }

    boolean hasArguments() {
        return weakArgs != null;
    }

    private Object[] getConcreteArgs() {
        if (weakArgs == null) return new Object[0];
        Object[] args = new Object[weakArgs.length];
        for (int i = 0; i < weakArgs.length; i++) {
            args[i] = weakArgs[i].get();
        }
        return args;
    }

    Class<?> getInvokedClass() {
        return invokedClass;
    }

    Method getInvokedMethod() {
        return invokedMethod;
    }

    Class<?> getReturnType() {
        return invokedMethod.getReturnType();
    }

    String getInvokedPropertyName() {
        if (invokedPropertyName == null) invokedPropertyName = IntrospectionUtil.getPropertyName(invokedMethod);
        return invokedPropertyName;
    }

    Object invokeOn(Object object) {
        try {
            return object == null ? null : invokedMethod.invoke(object, getConcreteArgs());
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (weakArgs == null) return invokedMethod.toString();
        StringBuilder sb = new StringBuilder(invokedMethod.toString());
        sb.append(" with args ");
        boolean first = true;
        for (ParameterReference arg : weakArgs) {
            sb.append(first ? "" : ", ").append(arg.get());
            first = false;
        }
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hashCode != 0) return hashCode;
        hashCode = 13 * invokedClass.hashCode() + 17 * invokedMethod.hashCode();
        if (weakArgs != null) hashCode += 19 * weakArgs.length;
        if (previousInvocation != null) hashCode += 23 * previousInvocation.hashCode();
        return hashCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        Invocation otherInvocation = (Invocation)object;
        return areNullSafeEquals(invokedClass, otherInvocation.getInvokedClass()) &&
                areNullSafeEquals(invokedMethod, otherInvocation.getInvokedMethod()) &&
                areNullSafeEquals(previousInvocation, otherInvocation.previousInvocation) &&
                Arrays.equals(weakArgs, otherInvocation.weakArgs);
    }

    static boolean areNullSafeEquals(Object first, Object second) {
        return first == second || (first != null && second != null && first.equals(second));
    }

    private static abstract class ParameterReference {
        protected abstract Object get();

        @Override
        public final boolean equals(Object obj) {
            return obj instanceof ParameterReference && areNullSafeEquals(get(), ((ParameterReference)obj).get());
        }
    }

    private static final class StrongParameterReference extends ParameterReference {
        private final Object strongRef;

        private StrongParameterReference(Object referent) {
            strongRef = referent;
        }

        protected Object get() {
            return strongRef;
        }
    }

    private static final class WeakParameterReference extends ParameterReference {
        private final WeakReference<Object> weakRef;

        private WeakParameterReference(Object referent) {
            weakRef = new WeakReference<Object>(referent);
        }

        protected Object get() {
            return weakRef.get();
        }
    }
}
