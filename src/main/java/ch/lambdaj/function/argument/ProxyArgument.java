// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;

import java.lang.ref.*;
import java.lang.reflect.*;

import ch.lambdaj.proxy.*;

/**
 * @author Mario Fusco
 */
class ProxyArgument extends InvocationInterceptor {
	
	private final Class<?> proxiedClass;
	
	private final WeakReference<InvocationSequence> invocationSequence;
	
	ProxyArgument(Class<?> proxiedClass, InvocationSequence invocationSequence) {
		this.proxiedClass = proxiedClass;
		this.invocationSequence = new WeakReference<InvocationSequence>(invocationSequence);
	}

    /**
     * {@inheritDoc}
     */
	public Object invoke(Object proxy, Method method, Object[] args) {
		if (method.getName().equals("hashCode")) return invocationSequence.hashCode();
		if (method.getName().equals("equals")) return invocationSequence.equals(args[0]);
		
		// Adds this invocation to the current invocation sequence and creates a new proxy propagating the invocation sequence
		return createArgument(method.getReturnType(), new InvocationSequence(invocationSequence.get(), new Invocation(proxiedClass, method, args)));
	}
}
