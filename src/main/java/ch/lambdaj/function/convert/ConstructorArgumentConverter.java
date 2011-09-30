// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.convert;

import ch.lambdaj.util.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Creates an object of the given Class by invoking its constructor passing to it the values taken
 * from the object to be converted using the given arguments.
 * @author Mario Fusco
 */
public class ConstructorArgumentConverter<F, T> implements Converter<F, T> {

    private Constructor<T> constructor;

    private final List<ArgumentConverter<F, Object>> argumentConverters = new LinkedList<ArgumentConverter<F, Object>>();

    public ConstructorArgumentConverter(Class<T> clazz, Object... arguments) {
        for (Constructor<?> c : clazz.getConstructors()) {
            if (isCompatible(c, arguments)) {
                this.constructor = (Constructor<T>)c;
                break;
            }
        }

        if (constructor == null)
            throw new IntrospectionException("Unable to find a constructor of " + clazz.getName() + " compatible with the given arguments");

        if (arguments != null)
            for (Object argument : arguments) { argumentConverters.add(new ArgumentConverter<F, Object>(argument)); }
    }

    private boolean isCompatible(Constructor<?> constructor, Object... arguments) {
        try {
            constructor.newInstance(arguments);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public T convert(F from) {
        Object[] initArgs = new Object[argumentConverters.size()];
        int i = 0;
        for (ArgumentConverter<F, Object> argumentConverter : argumentConverters) {
            initArgs[i++] = argumentConverter.convert(from);
        }
        try {
            return constructor.newInstance(initArgs);
        } catch (Exception e) {
            throw new IntrospectionException("Unable to create an object of class " + constructor.getDeclaringClass().getName(), e);
        }
    }
}
