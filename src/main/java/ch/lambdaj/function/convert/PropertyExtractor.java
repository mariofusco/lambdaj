// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

import ch.lambdaj.util.IntrospectionUtil;


/**
 * Converts an Object in the value it has on the name property.
 * @author Mario Fusco
 */
public class PropertyExtractor<F, T> implements Converter<F, T> {

	private final String propertyName;
	
    /**
     * Creates a PropertyExtractor
     */
	public PropertyExtractor(String propertyName) {
		this.propertyName = propertyName; 
	}
	
    /**
     * {@inheritDoc}
     */
	@SuppressWarnings("unchecked")
	public T convert(F from) {
		return (T)IntrospectionUtil.getPropertyValue(from, propertyName);
	}

}
