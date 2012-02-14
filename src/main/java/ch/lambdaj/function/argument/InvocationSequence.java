// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Registers a sequence of method invocations
 *
 * @author Mario Fusco
 * @author Frode Carlsen
 */
final class InvocationSequence implements Invoker {

    private static boolean jittingEnabled = false;
    private static ExecutorService executor;

    static void enableJitting(boolean enable) {
        if (enable) {
            jittingEnabled = true;
            if (executor == null) {
                executor = Executors.newCachedThreadPool(new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setDaemon(true);
                        return t;
                    }
                });
            }
        } else {
            jittingEnabled = false;
            if (executor != null) {
                executor.shutdown();
                executor = null;
            }
        }
    }

    private final Class<?> rootInvokedClass;
    private String inkvokedPropertyName;
    Invocation lastInvocation;
    private int hashCode;

    private boolean jitDone;
    private AtomicBoolean needsJitting;

    private Invoker invoker = this;

    InvocationSequence(Class<?> rootInvokedClass) {
        this.rootInvokedClass = rootInvokedClass;
        jitDone = true;
    }

    InvocationSequence(InvocationSequence sequence, Invocation invocation) {
        rootInvokedClass = sequence.getRootInvokedClass();
        invocation.previousInvocation = sequence.lastInvocation;
        lastInvocation = invocation;
        boolean isJittable = jittingEnabled && isJittable(lastInvocation);
        if (isJittable) needsJitting = new AtomicBoolean(isJittable);
        jitDone = !isJittable;
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

    public Object evaluate(final Object object) {
        if (!jitDone && needsJitting.compareAndSet(true, false)) {
            jitDone = true;
            if (executor != null) {
                executor.submit(new Runnable() {
                    public void run() {
                        invoker = new InvokerJitter(object, InvocationSequence.this).jitInvoker();
                    }
                });
            }
        }
        return invoker.invokeOn(object);
    }

    public Object invokeOn(Object object) {
        return invokeOn(lastInvocation, object);
    }

    private Object invokeOn(Invocation invocation, Object value) {
        if (invocation == null) return value;
        if (invocation.previousInvocation != null) value = invokeOn(invocation.previousInvocation, value);
        return invocation.invokeOn(value);
    }

    private boolean isJittable(Invocation invocation) {
        return !invocation.hasArguments() && (invocation.previousInvocation == null || isJittable(invocation.previousInvocation));
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