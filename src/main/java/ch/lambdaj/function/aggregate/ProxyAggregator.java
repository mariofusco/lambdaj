// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import ch.lambdaj.util.iterator.*;
import ch.lambdaj.proxy.*;

/**
 * Proxies a list of objects in order to seamlessly aggregate their value by exposing the API of a single object.
 * @author Mario Fusco
 */
public class ProxyAggregator<T, A> extends ProxyIterator<T> {

	private final Aggregator<A> aggregator;

	protected ProxyAggregator(ResettableIterator<T> proxiedIterator, Aggregator<A> aggregator) {
		super(proxiedIterator);
		this.aggregator = aggregator;
	}

	@Override
	public Object invoke(Object obj, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
		if(enabled) return normalizeResult(method.getReturnType(), aggregator.aggregate((Iterator<A>)iterateOnValues(method, args)));
	    return null;
    }

    private Object normalizeResult(Class<?> expectedResultType, Object result) {
        if (result == null) return normalizeNullResult(expectedResultType, result);
        if (expectedResultType.isInstance(result)) return result;
        if (expectedResultType == BigInteger.class) return new BigInteger(result.toString());
        if (expectedResultType == BigDecimal.class) return new BigDecimal(result.toString());
        return result;
    }

    private Object normalizeNullResult(Class<?> expectedResultType, Object result) {
        if (expectedResultType == BigInteger.class) return BigInteger.ZERO;
        if (expectedResultType == BigDecimal.class) return BigDecimal.ZERO;
        return result;
    }

    @SuppressWarnings("unchecked")
	public static <T, A> T createProxyAggregator(ResettableIterator<T> proxiedIterator, Aggregator<A> aggregator, Class<?> clazz) {
		return (T)ProxyUtil.createIterableProxy(new ProxyAggregator<T, A>(proxiedIterator, aggregator), clazz);
	}
}
