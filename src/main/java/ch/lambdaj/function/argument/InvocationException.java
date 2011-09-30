// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

import java.lang.reflect.*;

/**
 * This Exception is thrown when a problem occurs while doing a via reflection method call through the {@link Invocation} class.
 * @author Mario Fusco
 */
public class InvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	InvocationException(Throwable t, Method invokedMethod, Object object) {
		super("Failed invocation of " + invokedMethod + " on object " + object + " caused by: " + t.getLocalizedMessage(), t);
	}
}
