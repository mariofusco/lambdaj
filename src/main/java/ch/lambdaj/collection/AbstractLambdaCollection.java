// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.*;

import java.util.*;

import org.hamcrest.*;

/**
 * The abstract class extended by all the the Lambda collections.
 * @author Mario Fusco
 */
class AbstractLambdaCollection<T> {

    Iterable<? extends T> innerIterable;
    Iterator<? extends T> innerIterator;

    AbstractLambdaCollection(Iterator<? extends T> innerIterator) {
        this.innerIterable = null;
        this.innerIterator = innerIterator;
    }

    AbstractLambdaCollection(Iterable<? extends T> innerIterable) {
        this.innerIterable = innerIterable;
        innerIterator = innerIterable.iterator();
    }

    void setInner(Iterable<? extends T> inner) {
        if (inner instanceof Collection && innerIterable instanceof Collection)
            setInnerCollection((Collection<? extends T>)inner);
        else
            innerIterable = inner;
        innerIterator = innerIterable.iterator();
    }

    private void setInnerCollection(Collection<? extends T> inner) {
        try {
            ((Collection<T>)innerIterable).clear();
            ((Collection<T>)innerIterable).addAll((Collection<T>)inner);
        } catch (UnsupportedOperationException e) {
            innerIterable = new LinkedList<T>((Collection<T>)inner);
        }
    }

    private Object getInner() {
        return innerIterable != null ? innerIterable : innerIterator;
    }

    private Iterator<? extends T> getInnerIterator() {
        return innerIterable != null ? innerIterable.iterator() : innerIterator;
    }

    /**
     * Aggregates the items in this iterable using the given {@link Aggregator}.
     * @param aggregator The function that defines how the objects in this iterable have to be aggregated
     * @return The result of the aggregation of all the items in this iterable
     */
    public T aggregate(Aggregator<T> aggregator) {
        return Lambda.aggregate(getInner(), aggregator);
    }

    /**
     * For each item in the given iterable collects the value defined by the given argument and
     * then aggregates them iterable using the given {@link Aggregator}.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @param aggregator The function that defines how the objects in this iterable have to be aggregated
     * @return The result of the aggregation of all the items in this iterable
     */
    public <A> A aggregate(A argument, Aggregator<A> aggregator) {
        return Lambda.aggregate(getInner(), aggregator, argument);
    }

    /**
     * Finds the minimum item in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The minimum of all the Object in the given iterable
     */
    public <A> A min(A argument) {
        return Lambda.min(getInner(), argument);
    }

    /**
     * Finds the maximum item in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The maximum of all the Object in the given iterable
     */
    public <A> A max(A argument) {
        return Lambda.max(getInner(), argument);
    }

    /**
     * Sums the property values of the items in this iterable defined by the given argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The sum of the property values extracted from all the items in the given iterable
     */
    public <A> A sum(A argument) {
        return Lambda.sum(getInner(), argument);
    }

    /**
     * Selects the item in this iterable having the lowest value on the given argument defined using the on method.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The item in the given iterable with the minimum value on the given argument
     */
    public <A> T selectMin(A argument) {
        return (T)Lambda.selectMin(getInner(), argument);
    }

    /**
     * Selects the item in this iterable having the highest value on the given argument defined using the on method.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The item in the given iterable with the maximum value on the given argument
     */
    public <A> T selectMax(A argument) {
        return (T)Lambda.selectMax(getInner(), argument);
    }

    /**
     * Joins all the object in this iterable by concatenating all their String representation.
     * It invokes toString() an all the objects and concatening them using the default separator ", ".
     * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
     */
    public String join() {
        return Lambda.join(getInner());
    }

    /**
     * Joins all the object in this iterable by concatenating all their String representation.
     * It invokes toString() an all the objects and concatening them using the given separator.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param separator The String used to separe the item's String representation
     * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
     */
    public String join(String separator) {
        return Lambda.join(getInner(), separator);
    }

    /**
     * Transforms this iterable in a single object having the same methods of a single object in this iterable.
     * That allows to invoke a method on each T in the collection with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     */
    public T forEach() {
        return Lambda.forEach(getInnerIterator());
    }

    /**
     * Transforms the subset of objects in this iterable that match the given Mathcer
     * in a single object having the same methods of a single object in this iterable.
     * That allows to invoke a method on each T in the collection with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     */
    public T forEach(Matcher<?> matcher) {
        return Lambda.forEach((List<T>)Lambda.select(getInner(), matcher));
    }

    /**
     * Maps the objects in this iterable on the value extracted using the given {@link Converter}.
     * @param converter The converter that specifies the key on which each object should be mapped
     * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
     */
    public <K> LambdaMap<K, T> map(Converter<T, K> converter) {
        return new LambdaMap<K, T>(Lambda.map(getInner(), converter));
    }

    /**
     * Indexes the objects in this iterable based on the value of their argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
     */
    @SuppressWarnings("unchecked")
    public <K> LambdaMap<K, T> map(K argument) {
        return new LambdaMap<K, T>((Map<K, T>) Lambda.index(getInner(), argument));
    }

    /**
     * Indexes the objects in this iterable based on the value of their argument.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
     */
    public <A> LambdaMap<A, T> index(A argument) {
        return new LambdaMap<A, T>((Map<A, T>)Lambda.index(getInner(), argument));
    }

    /**
     * Returns true if exists at least one item in this iterable that matches the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to match the items in this iterable
     * @return True if exists at least one item in this iterable that matches the given hamcrest Matcher, false otherwise
     */
    public boolean exists(Matcher<?> matcher) {
        return first(matcher) != null;
    }

    /**
     * Returns true if all the items in this iterable match the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to match the items in this iterable
     * @return True if all the items in this iterable match the given hamcrest Matcher, false otherwise
     */
    public boolean all(Matcher<?> matcher) {
        Iterator<? extends T> iterator = getInnerIterator();
        while (iterator.hasNext()) { if (!matcher.matches(iterator.next())) return false; }
        return true;
    }

    /**
     * Selects the first object in this iterable that matches the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to retain the given iterable
     * @return The first object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
     */
    public T first(Matcher<?> matcher) {
        return (T)Lambda.selectFirst(getInner(), matcher);
    }

    /**
     * Selects the unique object in this iterable that matches the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to retain the given iterable
     * @return The only object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
     * @throws RuntimeException if there is more than one object that matches the given hamcrest Matcher
     */
    public T unique(Matcher<?> matcher) {
        return (T)Lambda.selectUnique(getInner(), matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        return o instanceof AbstractLambdaCollection ? getInner().equals(((AbstractLambdaCollection)o).getInner()) : getInner().equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return getInner().hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getInner().toString();
    }
}
