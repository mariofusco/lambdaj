// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

/**
 * Converts an object in its String representation or an empty String for a null object.
 * @author Mario Fusco
 */
public class DefaultStringConverter implements StringConverter<Object> {

    /**
     * {@inheritDoc}
     */
	public String convert(Object from) {
		return from == null ? "" : from.toString();
	}

}
