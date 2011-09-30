// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;


/**
 * Converts a String in an Integer equal to its length.
 * @author Mario Fusco
 */
public class StringLengthConverter extends PropertyExtractor<String, Integer> {

    /**
     * Creates a StringLengthConverter
     */
	public StringLengthConverter() {
		super("length");
	}
}
