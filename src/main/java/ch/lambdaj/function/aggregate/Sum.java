// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.aggregate;

import java.math.*;

/**
 * An aggregator that sums numbers
 * @author Mario Fusco
 */
public class Sum extends InitializedPairAggregator<Number> {

    /**
     * Creates an aggregator that sums numbers
     */
    public Sum() {
        super(0);    
    }

    /**
     * Creates an aggregator that sums numbers
     * @param firstItem The first number to be summed
     */
    public Sum(Number firstItem) {
        super(firstItem);
    }

    /**
     * Aggregates two Number by summing them
     * @param first The first number to be summed
     * @param second The second number to be summed
     * @return The sum of the two Numbers
     */
	public Number aggregate(Number first, Number second) {
		if (second == null) return first == null ? 0.0 : first;

		if (second instanceof Integer) return aggregate(first, (Integer)second);
		if (second instanceof Long) return aggregate(first, (Long)second);
		if (second instanceof Float) return aggregate(first, (Float)second);
		if (second instanceof Double) return aggregate(first, (Double)second);
        if (second instanceof BigInteger) return aggregate(first, (BigInteger)second);
        if (second instanceof BigDecimal) return aggregate(first, (BigDecimal)second);

		throw new RuntimeException("unable to aggregate " + first + " and " + second);
	}

	private Integer aggregate(Number first, Integer second) {
		return (first == null ? emptyItem().intValue() : first.intValue()) + second;
	}

	private Long aggregate(Number first, Long second) {
		return (first == null ? emptyItem().longValue() : first.longValue()) + second;
	}

	private Float aggregate(Number first, Float second) {
		return (first == null ? emptyItem().floatValue() : first.floatValue()) + second;
	}

	private Double aggregate(Number first, Double second) {
		return (first == null ? emptyItem().doubleValue() : first.doubleValue()) + second;
	}

    private BigInteger aggregate(Number first, BigInteger second) {
        return (first == null || !(first instanceof BigInteger) ? BigInteger.ZERO : (BigInteger)first).add(second);
    }

    private BigDecimal aggregate(Number first, BigDecimal second) {
        return (first == null || !(first instanceof BigDecimal) ? BigDecimal.ZERO : (BigDecimal)first).add(second);
    }
}
