// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.argument;

/**
 *
 * @author Mario Fusco
 */
public interface FinalClassArgumentCreator<T> {

    /**
     * Create a placeholder for an argument of the final class T using the given seed.
     * @param seed  The seed to generate the unique placeholder
     * @return A placeholder for an argument of class T
     */
    T createArgumentPlaceHolder(int seed);
}
