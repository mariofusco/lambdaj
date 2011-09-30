// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

/**
 * Registers a sequence of method invocations
 *
 * @author Mario Fusco
 * @author Frode Carlsen
 */
final class InvocationSequence {
    private static final long serialVersionUID = 1L;

    private final Class<?> rootInvokedClass;
    private String inkvokedPropertyName;
    private Invocation lastInvocation;
    private transient int hashCode;

    InvocationSequence(Class<?> rootInvokedClass) {
        this.rootInvokedClass = rootInvokedClass;
    }

    InvocationSequence(InvocationSequence sequence, Invocation invocation) {
        this(sequence.getRootInvokedClass());
        invocation.previousInvocation = sequence.lastInvocation;
        lastInvocation = invocation;
    }

    Class<?> getRootInvokedClass() {
        return rootInvokedClass;
    }

    String getInkvokedPropertyName() {
        if (inkvokedPropertyName == null) inkvokedPropertyName = calcInkvokedPropertyName();
        return inkvokedPropertyName;
    }

    private String calcInkvokedPropertyName() {
        if (null == lastInvocation) return "";
        StringBuilder sb = new StringBuilder();

        calcInkvokedPropertyName(lastInvocation, lastInvocation.previousInvocation, sb);
        return sb.substring(1);
    }

    private void calcInkvokedPropertyName(Invocation inv, Invocation prevInv, StringBuilder sb) {
        if (prevInv != null) {
            calcInkvokedPropertyName(prevInv, prevInv.previousInvocation, sb);
        }
        sb.append(".").append(inv.getInvokedPropertyName());
    }

    Class<?> getReturnType() {
        return lastInvocation.getReturnType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        return object != null && rootInvokedClass == ((InvocationSequence)object).rootInvokedClass
            && Invocation.areNullSafeEquals(lastInvocation, ((InvocationSequence)object).lastInvocation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hashCode != 0) return hashCode;
        hashCode = 13 * rootInvokedClass.hashCode();
        int factor = 17;
        for (Invocation invocation = lastInvocation; invocation != null; invocation = invocation.previousInvocation) {
            hashCode += factor * invocation.hashCode();
            factor += 2;
        }
        return hashCode;
    }

    @SuppressWarnings("unchecked")
    public <T> T invokeOn(Object object) {
        return (T)invokeOn(lastInvocation, object);
    }

    private Object invokeOn(Invocation invocation, Object value) {
        if (invocation == null) return value;
        if (invocation.previousInvocation != null) value = invokeOn(invocation.previousInvocation, value);
        return invocation.invokeOn(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("[");
        if (lastInvocation == null) sb.append(rootInvokedClass);
        else toString(lastInvocation, lastInvocation.previousInvocation, sb, true);
        sb.append("]");
        return sb.toString();
    }

    private void toString(Invocation inv, Invocation prevInv, StringBuilder sb, boolean first) {
        if (prevInv != null) toString(prevInv, prevInv.previousInvocation, sb, false);
        sb.append(inv);
        if (!first) sb.append(", ");
    }
}