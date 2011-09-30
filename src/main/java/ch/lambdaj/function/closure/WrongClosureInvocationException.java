// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

/**
 * This Exception is thrown when a closure gets invoked with a wrong number or type of variables
 * @author Mario Fusco
 */
public class WrongClosureInvocationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	WrongClosureInvocationException(String message) {
		super(message);
	}

	WrongClosureInvocationException(String message, Throwable cause) {
		super(message, cause);
	}
}
