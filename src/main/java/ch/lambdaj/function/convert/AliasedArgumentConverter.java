// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import ch.lambdaj.function.argument.*;

import java.util.*;

/**
 * Converts an object in a key/value pair where the value is the one returned by the evaluation of the given argument on the object itself.
 * @author Mario Fusco
 */
public class AliasedArgumentConverter<F, T> implements Converter<F, Map.Entry<String, T>> {

    private final String alias;
    private final Argument<T> argument;

    /**
     * Creates a converter that projects the value of the argument of an object using as alias
     * the argument property name as defined by {@link Argument#getInkvokedPropertyName()}
     * @param argument An argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method
     */
    public AliasedArgumentConverter(T argument) {
        this.argument = actualArgument(argument);
        alias = this.argument.getInkvokedPropertyName();
    }

    /**
     * Creates a converter that projects the value of the argument of an object using as the given alias
     * @param alias The key on which the argument value is paired
     * @param argument An argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method
     */
    public AliasedArgumentConverter(String alias, T argument) {
        this.argument = actualArgument(argument);
        this.alias = alias;
    }

    /**
     * {@inheritDoc}
     */
    public Map.Entry<String, T> convert(final F from) {
        return new Map.Entry<String, T>() {
            public String getKey() { return alias; }
            public T getValue() { return argument.evaluate(from); }
            public T setValue(T value) { throw new UnsupportedOperationException(); }
        };
    }
}
