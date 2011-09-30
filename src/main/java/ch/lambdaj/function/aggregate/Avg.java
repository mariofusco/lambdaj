package ch.lambdaj.function.aggregate;

import java.math.*;
import java.util.*;

/**
 * An aggregator calculating numbers' average
 * @author Mario Fusco
 */
public class Avg extends Sum {

    int itemsCounter = 0;

    /**
     * {@inheritDoc}
     */
    public Number aggregate(Iterator<? extends Number> iterator) {
        return divide(super.aggregate(iterator), itemsCounter);
    }

    /**
     * {@inheritDoc}
     */
    public Number aggregate(Number first, Number second) {
        itemsCounter++;
        return super.aggregate(first, second);
    }

    private Number divide(Number total, int count) {
        if (count < 2) return total;
        if (total instanceof Integer) return total.intValue() / count;
        if (total instanceof Long) return total.longValue() / count;
        if (total instanceof Float) return total.floatValue() / count;
        if (total instanceof Double) return total.doubleValue() / count;
        if (total instanceof BigInteger) return ((BigInteger)total).divide(BigInteger.valueOf(count));
        if (total instanceof BigDecimal) return ((BigDecimal)total).divide(new BigDecimal(count));
        throw new RuntimeException("Unknown number type");
    }
}
