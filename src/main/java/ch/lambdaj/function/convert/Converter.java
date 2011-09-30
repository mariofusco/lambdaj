// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

/** 
 * Specifies how to convert from an object of type F to an object of type T
 * @author Mario Fusco
 */
public interface Converter<F, T> {

	/**
	 * Converts from an object of type F to an object of type T
	 * @param from The object to be converted
	 * @return The conversion result
	 */
	T convert(F from);
}
