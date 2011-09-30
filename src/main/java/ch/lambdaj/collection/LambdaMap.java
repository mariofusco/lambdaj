// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.convertMap;
import static org.hamcrest.Matchers.not;

import java.io.*;
import java.util.*;

import ch.lambdaj.function.convert.Converter;
import ch.lambdaj.util.*;
import org.hamcrest.*;

/**
 * A Map that extends the Map interface with the fluent interface methods provided by lambdaj
 * @author Gianfranco Tognana
 * @author Mario Fusco
 */
public class LambdaMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    
	private Map<K, V> innerMap;
	
	LambdaMap(Map<? extends K, ? extends V> innerMap) {
		this.innerMap = (Map<K, V>)innerMap;
	}
	
    /**
	 * Converts all the values in this map using the given {@link Converter}.
     * @param converter The converter that specifies how each map's value must be converted
	 * @return A Map containing the same keys of the original one and the value converted from the ones
     *      in the corresponding entry of the map
	 */
    public <T> LambdaMap<K, T> convertValues(Converter<V, T> converter) {
        return new LambdaMap<K, T>(convertMap(innerMap, converter));
    }

    /**
	 * Converts all the values in this map extracting the property defined by the given argument.
	 * @param argument An argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method method
	 * @return A Map containing the same keys of the original one and the argument's value extracted from the value
     *      in the corresponding entry of the map
	 */
	public <T> LambdaMap<K, T> convertValues(T argument) {
		return new LambdaMap<K, T>(convertMap(innerMap, argument));
	}

    /**
     * Retains only the entries in this map having a key that matches the given matcher
     * @param matcher The matcher
     * @return This map without the keys that don't match the given matcher
     */
    public LambdaMap<K, V> retainKeys(Matcher<?> matcher) {
        return retain(matcher, true);
    }

    /**
     * Remove the entries in this map having a key that matches the given matcher
     * @param matcher The matcher
     * @return This map with only the keys that don't match the given matcher
     */
    public LambdaMap<K, V> removeKeys(Matcher<?> matcher) {
        return retainKeys(not(matcher));
    }

    /**
     * Retains only the entries in this map having a value that matches the given matcher
     * @param matcher The matcher
     * @return This map without the values that don't match the given matcher
     */
    public LambdaMap<K, V> retainValues(Matcher<?> matcher) {
        return retain(matcher, false);
    }

    /**
     * Remove the entries in this map having a value that matches the given matcher
     * @param matcher The matcher
     * @return This map with only the values that don't match the given matcher
     */
    public LambdaMap<K, V> removeValues(Matcher<?> matcher) {
        return retainValues(not(matcher));
    }

    private LambdaMap<K, V> retain(Matcher<?> matcher, boolean matchKeys) {
        Map<K, V> map = new HashMap<K, V>();
        for (Entry<K, V> entry : innerMap.entrySet()) {
            if (matcher.matches(matchKeys ? entry.getKey() : entry.getValue())) map.put(entry.getKey(), entry.getValue());
        }
        try {
            innerMap.clear();
            innerMap.putAll(map);
        } catch (UnsupportedOperationException e) { innerMap = map; }
        return this;
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Map interface
    // ////////////////////////////////////////////////////////////////////////

    /**
     * {@inheritDoc}
     */
	public void clear() {
	    innerMap.clear();
    }

    /**
     * {@inheritDoc}
     */
	public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
	public boolean containsValue(Object value) {
	    return innerMap.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
	public LambdaSet<java.util.Map.Entry<K, V>> entrySet() {
	    return new LambdaSet<java.util.Map.Entry<K, V>>(innerMap.entrySet());
    }

    /**
     * {@inheritDoc}
     */
	public boolean equals(Object o) {
	    return innerMap.equals(o);
    }

    /**
     * {@inheritDoc}
     */
	public V get(Object key) {
	    return innerMap.get(key);
    }

    /**
     * {@inheritDoc}
     */
	public int hashCode() {
	    return innerMap.hashCode();
    }

    /**
     * {@inheritDoc}
     */
	public boolean isEmpty() {
	    return innerMap.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
	public LambdaSet<K> keySet() {
	    return new LambdaSet<K>(innerMap.keySet());
    }

    /**
     * {@inheritDoc}
     */
	public V put(K key, V value) {
	    return innerMap.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
	public void putAll(Map<? extends K, ? extends V> m) {
	    innerMap.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
	public V remove(Object key) {
	    return innerMap.remove(key);
    }

    /**
     * {@inheritDoc}
     */
	public int size() {
	    return innerMap.size();
    }

    /**
     * {@inheritDoc}
     */
	public LambdaCollection<V> values() {
	    return new LambdaCollection(innerMap.values());
    }

    /**
     * Returns a shallow copy of this LambdaMap instance. (The elements themselves are not copied.)
     * @return A clone of this LambdaMap instance
     */
    @Override
    public LambdaMap<K, V> clone() {
        return clone(new HashMap<K, V>());
    }

    /**
     * Returns a shallow copy of this LambdaMap instance. (The elements themselves are not copied.)
     * @param emptyMap The empty map to be used as wrapped set of this LambdaMap if the current one is not Cloneable
     * @return A clone of this LambdaMap instance
     */
    public LambdaMap<K, V> clone(Map<K, V> emptyMap) {
        try {
            return new LambdaMap<K, V>((Map<K, V>)IntrospectionUtil.clone(innerMap));
        } catch (CloneNotSupportedException e) { }
        emptyMap.putAll(innerMap);
        return new LambdaMap<K, V>(emptyMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return innerMap.toString();
    }
}
