// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

/**
 * This exception is thrown when is not possible to convert a placeholder object in an Argument
 * @author Mario Fusco
 */
public class ArgumentConversionException extends RuntimeException {

    ArgumentConversionException(String message) {
    	super(message);
    }

    ArgumentConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
