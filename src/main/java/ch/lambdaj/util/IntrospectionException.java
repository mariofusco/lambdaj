// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util;

/**
 * This Exception is thrown when something in the introspection process goes wrong.
 * @author Mario Fusco
 */
public class IntrospectionException extends RuntimeException {

    /**
     * Constructs a new IntrospectionException with the specified cause.
     * @param cause The root cause of this Exception
     */
    public IntrospectionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new IntrospectionException with the specified message.
     * @param message The message of this Exception
     */
    public IntrospectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new IntrospectionException with the specified cause and message.
     * @param cause The root cause of this Exception
     * @param message The message of this Exception
     */
    public IntrospectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
