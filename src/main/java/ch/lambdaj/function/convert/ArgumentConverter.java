// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

/**
 * Converts an object in the value returned by the evaluation of the given argument on it.
 * @author Mario Fusco
 */
public class ArgumentConverter<F, T> implements Converter<F, T> {

	private final Argument<T> argument;
	
    /**
     * Creates an ArgumentConverter
     */
	public ArgumentConverter(Argument<T> argument) {
		this.argument = argument;
	}
	
    /**
     * Creates an ArgumentConverter
     */
	public ArgumentConverter(T argument) {
		this(actualArgument(argument));
	}
	
    /**
     * {@inheritDoc}
     */
	public T convert(F from) {
		return argument.evaluate(from);
	}

}
