// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;

import java.lang.reflect.*;

import net.sf.cglib.proxy.*;

/**
 * An intercptor that seamlessly manages invocations on both a native Java proxy and a cglib one.
 * @author Mario Fusco
 */
public abstract class InvocationInterceptor implements MethodInterceptor, java.lang.reflect.InvocationHandler {

    /**
     * {@inheritDoc}
     */
	public final Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return invoke(proxy, method, args);
	}
}
