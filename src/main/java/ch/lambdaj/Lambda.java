// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import static ch.lambdaj.function.argument.ArgumentsFactory.*;
import static ch.lambdaj.function.closure.ClosuresFactory.*;
import static ch.lambdaj.function.compare.ComparatorUtil.getStandardComparator;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.*;
import static ch.lambdaj.util.iterator.IteratorFactory.*;

import ch.lambdaj.util.*;
import ch.lambdaj.util.iterator.*;

import java.math.*;
import java.util.*;

import org.hamcrest.*;

import ch.lambdaj.function.aggregate.*;
import ch.lambdaj.function.argument.*;
import ch.lambdaj.function.closure.*;
import ch.lambdaj.function.compare.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.function.matcher.*;
import ch.lambdaj.proxy.*;
import ch.lambdaj.group.*;

/**
 * This class consists exclusively of static methods that allow to use all the core features of the lambdaj library.
 * @author Mario Fusco
 */
@SuppressWarnings("unchecked")
public final class Lambda {

    private Lambda() { }

    public static int jitThreshold = 0;

	/**
	 * Constructs a proxy object that mocks the given Class registering all the subsequent invocations on the object.
	 * @param clazz The class of the object to be mocked
	 * @return An object of the given class that register all the invocations made on it
	 */
	public static <T> T on(Class<T> clazz) {
		return createArgument(clazz);
	}
	
	/**
	 * Returns the actual argument of the methods invocation sequence defined through the {@link Lambda#on(Class)} method.
	 * @param argumentPlaceholder The placeholder for this argument created using the {@link Lambda#on(Class)} method
     * @return The actual argument of the methods invocation sequence defined through the {@link Lambda#on(Class)} method
	 */
	public static <T> Argument<T> argument(T argumentPlaceholder) {
		return actualArgument(argumentPlaceholder);
	}

	/**
	 * Transforms a collection of Ts in a single object having the same methods of a single instance of T.
	 * That allows to invoke a method on each T in the collection with a single strong typed method call as in the following example:
	 * <pre>
	 * 		List&lt;Person&gt; personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
	 *		forEach(personInFamily).setLastName("Fusco");
	 * </pre>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param <T> The type of the items in the iterable
	 * @param iterable The iterable to be transformed
	 * @return An object that proxies all the item in the iterable or null if the iterable is null or empty
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T forEach(Iterable<? extends T> iterable) {
        ResettableIterator<T> resettableIterator = (ResettableIterator<T>)asResettableIterator(iterable);
        if (!resettableIterator.hasNext())
            throw new IllegalArgumentException("forEach() is unable to introspect on an empty iterator. Use the overloaded method accepting a class instead");
        return ProxyIterator.createProxyIterator(resettableIterator, resettableIterator.next());
	}

    /**
     * Transforms a collection of Ts in a single object having the same methods of a single instance of T.
     * That allows to invoke a method on each T in the collection with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @param <T> The type of the items in the iterable
     * @param iterator The iterator to be transformed
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     * @throws IllegalArgumentException if the iterable is null or empty
     */
    public static <T> T forEach(Iterator<? extends T> iterator) {
        if (!iterator.hasNext())
            throw new IllegalArgumentException("forEach() is unable to introspect on an empty iterator. Use the overloaded method accepting a class instead");
        ResettableIterator<T> resettableIterator = (ResettableIterator<T>)asResettableIterator(iterator);
		return ProxyIterator.createProxyIterator(resettableIterator, resettableIterator.next());
    }

	/**
	 * Transforms a collection of Ts in a single object having the same methods of a single instance of T.
	 * That allows to invoke a method on each T in the collection with a single strong typed method call as in the following example:
	 * <pre>
	 * 		List&lt;Person&gt; personInFamily = asList(new Person("Domenico"), new Person("Mario"), new Person("Irma"));
	 *		forEach(personInFamily, Person.class).setLastName("Fusco");
	 * </pre>
	 * The given class represents the proxied by the returned object, so it should be a superclass of all the objects in the iterable.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param <T> The type of the items in the iterable
	 * @param iterable The iterable to be transformed
	 * @param clazz The class proxied by the returned object
	 * @return An object that proxies all the item in the iterable. If the given iterable is null or empty it returns
	 * an instance of T that actually proxies an empty Iterable of Ts
	 */
	public static <T> T forEach(Iterable<? extends T> iterable, Class<T> clazz) {
        return ProxyIterator.createProxyIterator((ResettableIterator<T>)asResettableIterator(iterable), clazz);
	}

    /**
     * Transforms an iterator of Ts in a single object having the same methods of a single instance of T.
     * That allows to invoke a method on each T in the iterator with a single strong typed method call.
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @param <T> The type of the items in the iterator
     * @param iterator The iterator to be transformed
     * @param clazz The class proxied by the returned object
     * @return An object that proxies all the item in the iterator or null if the iterator is null or empty
     * @throws IllegalArgumentException if the iterator is null or empty
     */
    public static <T> T forEach(Iterator<? extends T> iterator, Class<T> clazz) {
        return ProxyIterator.createProxyIterator((ResettableIterator<T>)asResettableIterator(iterator), clazz);
    }

    /**
     * Transforms an array of Ts in a single object having the same methods of a single instance of T.
     * That allows to invoke a method on each T in the array with a single strong typed method call.
     * @param <T> The type of the items in the array
     * @param array The array to be transformed
     * @return An object that proxies all the item in the array
     */
    public static <T> T forEach(T... array) {
        return forEach((Class<T>)array[0].getClass(), array);
    }

    /**
     * Transforms an array of Ts in a single object having the same methods of a single instance of T.
     * That allows to invoke a method on each T in the array with a single strong typed method call.
     * @param <T> The type of the items in the array
     * @param clazz The class proxied by the returned object
     * @param array The array to be transformed
     * @return An object that proxies all the item in the array
     */
    public static <T> T forEach(Class<T> clazz, T... array) {
        return ProxyIterator.createProxyIterator((ResettableIterator<T>)asResettableIterator(array), clazz);
    }

	// ////////////////////////////////////////////////////////////////////////
	// /// Collection
	// ////////////////////////////////////////////////////////////////////////

    /**
     * Flattens the given iterable by recursively descending through its nested Collections
     * and create a flat List of all of the leaves.
     * This method also works with Maps (by collecting their values) and arrays.
     * @param iterable The iterable to be flattened
     * @return The flattened iterable
     */
    public static <T> List<T> flatten(Object iterable) {
        return flattenIterator(iterable);
    }

	/**
	 * Collects the items in the given iterable putting them in a List.
	 * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of which the items should be collected
	 * @return A List containing all the items collected from the give iterable
	 * @throws IllegalArgumentException if the iterable is not an Iterable or a Map
	 */
	public static <T> List<? extends T> collect(Object iterable) {
		List<T> collected = new LinkedList<T>();
        Iterator i = asIterator(iterable);
		while (i.hasNext()) {
            Object item = i.next();
			if (item instanceof Iterable) collected.addAll((Collection<T>) collect(item));
			else if (item instanceof Map) collected.addAll((Collection<T>) collect(((Map<?,?>)item).values()));
			else collected.add((T)item);
		}
		return collected;
	}
	
	/**
	 * For each item in the given iterable collects the value defined by the given argument and put them in a List.
	 * For example the following code:
	 * <pre>
	 * 		List&lt;Person&gt; myFriends = asList(new Person("Biagio", 39), new Person("Luca", 29), new Person("Celestino", 29));
	 *		List&lt;Integer&gt; ages = collect(meAndMyFriends, on(Person.class).getAge());
	 * </pre>
	 * extracts the ages of all the Persons in the list and put them in a List of Integer.
	 * <p/>
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of which the items should be collected
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return A List containing all the items collected from the give iterable
	 * @throws RuntimeException if the iterable is not an Iterable or a Map
	 */
	public static <T> List<T> collect(Object iterable, T argument) {
		return (List<T>)collect(convert(iterable, new ArgumentConverter<Object, T>(argument)));
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Sort
	// ////////////////////////////////////////////////////////////////////////

    public static final int DESCENDING = 1;
    public static final int IGNORE_CASE = 2;

	/**
	 * Sorts all the items in the given iterable on the respective values of the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be sorted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return A List with the same items of the given iterable sorted on the respective value of the given argument
	 */
	public static <T> List<T> sort(Object iterable, Object argument) {
		return sort(iterable, argument, 0);
	}

    /**
     * Sorts all the items in the given iterable on the respective values of the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable of objects to be sorted
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @param option  Sorting option e.g.: DESCENDING + IGNORE_CASE
     * @return A List with the same items of the given iterable sorted on the respective value of the given argument
     */
    public static <T> List<T> sort(Object iterable, Object argument, int option) {
        return sort(iterable, argument, getStandardComparator(option));
    }

	/**
	 * Sorts all the items in the given iterable on the respective values of the given argument comparing them with the given comparator.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be sorted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @param comparator The comparator to determine the order of the list. A null value indicates that the elements' natural ordering should be used
	 * @return A List with the same items of the given iterable sorted on the respective value of the given argument
	 */
	public static <T, A> List<T> sort(Object iterable, A argument, Comparator<A> comparator) {
		List<T> sorted = new LinkedList<T>();
        for (Iterator<?> i = asIterator(iterable); i.hasNext();) { sorted.add((T)i.next()); }
		Collections.sort(sorted, new ArgumentComparator<T, A>(argument, comparator));
		return sorted;
	}

    /**
     * Counts the number of occurrencies of the objects in the given iterable
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable of objects to be counted
     * @return A map having as values the number of occurrencies of the corresponding key in the given iterable
     */
    public static <T> Map<T, Integer> count(Object iterable) {
        Map<T, Integer> countMap = new HashMap<T, Integer>();
        for (Iterator<?> i = asIterator(iterable); i.hasNext();) {
            T item = (T)i.next();
            Integer counter = countMap.get(item);
            countMap.put(item, counter == null ? 1 : counter+1);
        }
        return countMap;
    }
	
    /**
     * Counts the number of occurrencies of the argument's value in the objects of the given iterable
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable of objects' arguments to be counted
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A map having as values the number of occurrencies of the corresponding object's argument in the given iterable
     */
    public static <A> Map<A, Integer> count(Object iterable, A argument) {
        return count(extract(iterable, argument));
    }

	// ////////////////////////////////////////////////////////////////////////
	// /// Selection
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Filters all the objects in the given iterable that match the given hamcrest Matcher
	 * @param matcher The hamcrest Matcher used to filter the given iterable
     * @param iterable The iterable of objects to be filtered
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> filter(Matcher<?> matcher, Iterable<T> iterable) {
		return select(iterable, matcher);
	}

    /**
     * Filters all the objects in the given array that match the given hamcrest Matcher
     * @param matcher The hamcrest Matcher used to filter the given array
     * @param array The array of objects to be filtered
     * @return A sublist of the given array containing all the objects that match the given hamcrest Matcher
     */
    public static <T> List<T> filter(Matcher<?> matcher, T... array) {
        return select(array, matcher);
    }

	/**
	 * Selects all the objects in the given iterator that match the given hamcrest Matcher
	 * @param iterator The iterator of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> select(Iterator<T> iterator, Matcher<?> matcher) {
		List<T> collected = new LinkedList<T>();
        if (iterator == null) return collected;
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (matcher.matches(item)) collected.add(item);
        }
		return collected;
	}

    /**
     * Selects all the objects in the given iterable that match the given hamcrest Matcher
     * @param iterable The iterable of objects to be filtered
     * @param matcher The hamcrest Matcher used to filter the given iterable
     * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
     */
    public static <T> List<T> select(Iterable<T> iterable, Matcher<?> matcher) {
        if (iterable == null) return new LinkedList<T>();
        return select(iterable.iterator(), matcher);
    }

	/**
	 * Selects all the objects in the given iterable that match the given hamcrest Matcher
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return A sublist of the given iterable containing all the objects that match the given hamcrest Matcher
	 */
	public static <T> List<T> select(Object iterable, Matcher<?> matcher) {
		return select((Iterator<T>)asIterator(iterable), matcher);
	}

    /**
     * Selects all the objects in the given iterable that match the given hamcrest Matcher
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * Unlike the method {@link #select(Iterable, Matcher)} this one doesn't build a new collection, and the
	 * selection is done while iterating the returned iterator.
     * @param iterable The iterable of objects to be filtered
     * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return An iterator containing all the objects in the given iterable converted using the given {@link Converter}
	 */
	public static <T> Iterator<T> selectIterator(Object iterable, Matcher<?> matcher) {
		return new MatchingIterator<T>((Iterator<T>) asIterator(iterable), matcher);
	}

	/**
	 * Selects the unique object in the given iterable that matches the given hamcrest Matcher
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The only object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 * @throws NotUniqueItemException if there is more than one object that matches the given hamcrest Matcher
	 */
	public static <T> T selectUnique(Object iterable, Matcher<?> matcher) {
        Iterator<T> iterator = new MatchingIterator(asIterator(iterable), matcher);
        if (!iterator.hasNext()) return null;
        T unique = iterator.next();
        if (iterator.hasNext()) throw new NotUniqueItemException();
        return unique;
	}

    /**
     * Returns true if the given iterable contains at least an item that matches the given hamcrest Matcher
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return True if the given iterable contains at least an item that matches the given hamcrest Matcher false otherwise
	 */
    public static boolean exists(Object iterable, Matcher<?> matcher) {
        return selectFirst(iterable, matcher) != null;
    }

    /**
	 * Selects the first object in the given iterable that matches the given hamcrest Matcher
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param matcher The hamcrest Matcher used to filter the given iterable
	 * @return The first object in the given iterable that matches the given hamcrest Matcher or null if there is no such object
	 */
	public static <T> T selectFirst(Object iterable, Matcher<?> matcher) {
        Iterator<T> iterator = (Iterator<T>)asIterator(iterable);
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (matcher.matches(item)) return item;
        }
		return null;
	}

	/**
	 * Filters away all the duplicated items in the given iterable.
	 * @param iterable The iterable of objects to be filtered
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(Iterable<T> iterable) {
		return selectDistinct(iterable, (Comparator<T>) null);
	}

	/**
	 * Filters away all the duplicated items in the given iterable.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(Object iterable) {
		return selectDistinct(iterable, (Comparator<T>) null);
	}

	/**
	 * Selects all the items in the given iterable having a different value in the named property.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param propertyName The name of the item's property on which the item must have no duplicated value
	 * @return A Collection with the same items of the given iterable but containing no duplicate values on the named property
	 */
	public static <T> Collection<T> selectDistinct(Object iterable, String propertyName) {
		return selectDistinct(iterable, new PropertyComparator<T>(propertyName));
	}

	/**
	 * Selects all the items in the given iterable having a different value on the given argument defined using the on method.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return A Collection with the same items of the given iterable but containing no duplicate values on the given argument
	 */
	public static <T, A> Collection<T> selectDistinctArgument(Object iterable, A argument) {
		return selectDistinct(iterable, new ArgumentComparator<T, A>(argument));
	}
	
	/**
	 * Filters away all the duplicated items in the given iterable based on the given comparator.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param comparator The comparator used to decide if 2 items are different or not
	 * @return A Collection with the same items of the given iterable but containing no duplicate elements
	 */
	public static <T> Collection<T> selectDistinct(Object iterable, Comparator<T> comparator) {
		Set<T> collected = comparator == null ? new HashSet<T>() : new TreeSet<T>(comparator);
        for (Iterator<T> i = (Iterator<T>)asIterator(iterable); i.hasNext();) { collected.add(i.next()); }
		return collected;
	}

	/**
	 * Selects the item in the given iterable having the lowest value on the given argument defined using the on method.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The item in the given iterable with the minimum value on the given argument
	 */
	public static <T, A> T selectMin(Object iterable, A argument) {
		return aggregate(iterable, new MinOnArgument<T, A>(argument));
	}
	
	/**
	 * Selects the item in the given iterable having the highest value on the given argument defined using the on method.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects to be filtered
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The item in the given iterable with the maximum value on the given argument
	 */
	public static <T, A> T selectMax(Object iterable, A argument) {
		return aggregate(iterable, new MaxOnArgument<T, A>(argument));
	}
	
	// ////////////////////////////////////////////////////////////////////////
	// /// Aggregation
	// ////////////////////////////////////////////////////////////////////////

	private static Aggregator<? extends Number> getSumAggregator(Object object) {
		if (object instanceof Integer) return new SumInteger((Integer)object);
		if (object instanceof Double) return new SumDouble((Double)object);
		if (object instanceof Long) return new SumLong((Long)object);
		return new Sum((Number)object);
	}

    private static final Sum SUM = new Sum();
	private static final Min MIN = new Min();
	private static final Max MAX = new Max();
	private static final Concat CONCAT = new Concat();

	/**
	 * Aggregates the items in the given iterable using the given {@link Aggregator}.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return The result of the aggregation of all the items in the given iterable
	 * @throws RuntimeException if the iterable is not an Iterable
	 */
	public static <T> T aggregate(Object iterable, Aggregator<T> aggregator) {
		return aggregator.aggregate((Iterator<T>)asIterator(iterable));
	}

	/**
	 * For each item in the given iterable collects the value defined by the given argument and 
	 * then aggregates them iterable using the given {@link Aggregator}.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The result of the aggregation of all the items in the given iterable
	 * @throws RuntimeException if the iterable is not an Iterable
	 */
	public static <T, A> T aggregate(Object iterable, Aggregator<T> aggregator, A argument) {
		return aggregate(convertIterator(iterable, new ArgumentConverter<T, A>(argument)), aggregator);
	}
	
	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		aggregateFrom : (aggregator, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item must be converted in the object to be aggregated.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * @param iterable The iterable of the objects to containing the property to be aggregated.
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return A proxy of the class of the first object in the iterable representing an aggregation lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T, A> T aggregateFrom(Iterable<T> iterable, Aggregator<A> aggregator) {
		return aggregateFrom(iterable, discoverGenericType(iterable), aggregator);
	}

    /**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		aggregateFrom : (aggregator, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item must be converted in the object to be aggregated.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be aggregated.
	 * @param clazz The class proxied by the returned object
	 * @param aggregator The function that defines how the objects in this iterable have to be aggregated
	 * @return A proxy of the class of the first object in the iterable representing an aggregation lambda function
	 */
	public static <T, A> T aggregateFrom(Iterable<T> iterable, Class<?> clazz, Aggregator<A> aggregator) {
		return ProxyAggregator.createProxyAggregator((ResettableIterator<T>) asResettableIterator(iterable), aggregator, clazz);
	}

	// -- (Sum) ---------------------------------------------------------------

	/**
	 * Sums the items in the given iterable of Numbers or the iterable itself if it actually is already a single number.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @return The sum of all the Number in the given iterable or the iterable itself if it actually is already a single number
	 * @throws IllegalArgumentException if the iterable is not neither an Iterable nor a Number
	 */
	public static Number sum(Object iterable) {
        return typedSum(iterable, Double.class);
	}

	/**
	 * Sums the property values of the items in the given iterable defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of items containing the property of which the values have to be summed.
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The sum of the property values extracted from all the items in the given iterable 
	 * @throws IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T sum(Object iterable, T argument) {
        return (T)typedSum(convertIterator(iterable, new ArgumentConverter<Object, T>(argument)), argument.getClass());
	}
	
    private static Number typedSum(Object iterable, Class<?> numberClass) {
        if (iterable instanceof Number) return (Number)iterable;
        Iterator<?> iterator = asIterator(iterable);
        return iterator.hasNext() ? aggregate(iterator, getSumAggregator(iterator.next())) : typedZero(numberClass);
    }

    private static Number typedZero(Class<?> numberClass) {
        if (numberClass == Long.class) return 0L;
        if (numberClass == Double.class) return 0.0;
        if (numberClass == Float.class) return 0.0f;
        if (BigInteger.class.isAssignableFrom(numberClass)) return BigInteger.ZERO;
        if (BigDecimal.class.isAssignableFrom(numberClass)) return BigDecimal.ZERO;
        return 0;
    }

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		sumFrom : (+, iterable) => lambda : (convert : object => number) => number
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a number.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		int totalAge = sumFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be summed.
	 * @return A proxy of the class of the first object in the iterable representing a sum lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T sumFrom(Iterable<T> iterable) {
		return aggregateFrom(iterable, SUM);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		sumFrom : (+, iterable) => lambda : (convert : object => number) => number
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a number.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		int totalAge = sumFrom(persons, Person.class).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be summed.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a sum lambda function
	 */
	public static <T> T sumFrom(Iterable<T> iterable, Class<?> clazz) {
		return aggregateFrom(iterable, clazz, SUM);
	}

	// -- (Avg) ---------------------------------------------------------------

    /**
     * Calculates the average of the items in the given iterable of Numbers or the iterable itself if it actually is already a single number.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable of numbers to be summed
     * @return The average of all the Number in the given iterable or the iterable itself if it actually is already a single number
     * @throws IllegalArgumentException if the iterable is not neither an Iterable nor a Number
     */
    public static Number avg(Object iterable) {
        return typedAvg(iterable, Double.class);
    }

    /**
     * Calculates the average of the property values of the items in the given iterable defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable of items containing the property for which the average of its the values has to be calculated.
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return The average of the property values extracted from all the items in the given iterable
     * @throws IllegalArgumentException if the iterable is not an Iterable
     */
    public static <T> T avg(Object iterable, T argument) {
        return (T)typedAvg(convertIterator(iterable, new ArgumentConverter<Object, T>(argument)), argument.getClass());
    }

    private static Number typedAvg(Object iterable, Class<?> numberClass) {
        if (iterable instanceof Number) return (Number)iterable;
        Iterator<?> iterator = asIterator(iterable);
        return iterator.hasNext() ? aggregate(iterator, new Avg()) : typedZero(numberClass);
    }

    /**
     * Returns a lambda function defined as:
     * <p/>
     * 		avgFrom : (avg, iterable) => lambda : (convert : object => number) => number
     * <p/>
     * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a number.
     * This is done by invoking on that returned object the method that returns the values of the property fro which the average has to be calculated as in the following example
     * <p/>
     * <code>
     * 		int averageAge = avgFrom(persons).getAge();
     * </code>
     * <p/>
     * The actual class of T is inferred from the class of the first iterable's item, but you can
     * specify a particular class by using the overloaded method.
     * @param iterable The iterable of the objects to containing the property for which the average has to be calculated.
     * @return A proxy of the class of the first object in the iterable representing a sum lambda function
     * @throws IllegalArgumentException if the iterable is null or empty
     */
    public static <T> T avgFrom(Iterable<T> iterable) {
        return aggregateFrom(iterable, new Avg());
    }

    /**
     * Returns a lambda function defined as:
     * <p/>
     * 		avgFrom : (avg, iterable) => lambda : (convert : object => number) => number
     * <p/>
     * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a number.
     * This is done by invoking on that returned object the method that returns the values of the property fro which the average has to be calculated as in the following example
     * <p/>
     * <code>
     * 		int averageAge = avgFrom(persons, Person.class).getAge();
     * </code>
     * <p/>
     * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
     * @param iterable The iterable of the objects to containing the property for which the average has to be calculated.
     * @param clazz The class proxied by the returned object
     * @return A proxy of the class of the first object in the iterable representing a sum lambda function
     */
    public static <T> T avgFrom(Iterable<T> iterable, Class<?> clazz) {
        return aggregateFrom(iterable, clazz, new Avg());
    }

	// -- (Min) ---------------------------------------------------------------

	/**
	 * Finds the minimum item in the given iterable.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of numbers to be summed
	 * @return The minimum of all the Object in the given iterable
	 * @throws IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T min(Object iterable) {
		return (T) aggregate(iterable, MIN);
	}

	/**
	 * Finds the minimum item in the given iterable defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the minimum should be found
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The minimum of all the Object in the given iterable
	 * @throws IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T min(Object iterable, T argument) {
		return (T)aggregate(iterable, MIN, argument);
	}
	
	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		minFrom : (min, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item 
	 * must be converted in the object of which a minimum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int minAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of objects on which the minimum should be found
	 * @return A proxy of the class of the first object in the iterable representing a min lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T minFrom(Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, MIN);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		minFrom : (min, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item 
	 * must be converted in the object of which a minimum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int minAge = minFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects containing the property of which the minimum should be found.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a min lambda function
	 */
	public static <T> T minFrom(Iterable<T> iterable, Class<?> clazz) {
		return (T) aggregateFrom(iterable, clazz, MIN);
	}

	// -- (Max) ---------------------------------------------------------------

	/**
	 * Finds the maximum item in the given iterable.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @return The maximum of all the Object in the given iterable
	 * @throws IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T max(Object iterable) {
		return (T) aggregate(iterable, MAX);
	}

	/**
	 * Finds the maximum item in the given iterable defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return The maximum of all the Object in the given iterable
	 * @throws IllegalArgumentException if the iterable is not an Iterable
	 */
	public static <T> T max(Object iterable, T argument) {
		return (T)aggregate(iterable, MAX, argument);
	}
	
	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		maxFrom : (max, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item 
	 * must be converted in the object of which a maximum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int maxAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of objects on which the maximum should be found
	 * @return A proxy of the class of the first object in the iterable representing a max lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T maxFrom(Iterable<T> iterable) {
		return (T) aggregateFrom(iterable, MAX);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		maxFrom : (max, iterable) => lambda : (convert : object => object) => object
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines how each item 
	 * must be converted in the object of which a maximum value needs to be found.
	 * This is done by invoking on that returned object the method that returns the values of the property to be aggregated.
	 * <p/>
	 * <code>
	 * 		int maxAge = maxFrom(persons).getAge();
	 * </code>
	 * <p/>
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects containing the property of which the maximum should be found.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a max lambda function
	 */
	public static <T> T maxFrom(Iterable<T> iterable, Class<?> clazz) {
		return (T) aggregateFrom(iterable, clazz, MAX);
	}

	// -- (Join) --------------------------------------------------------------

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons).getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T joinFrom(Iterable<T> iterable) {
		return aggregateFrom(iterable, CONCAT);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, " - ").getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param separator The String used to separe the Strings produced by this lambda function
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 * @throws IllegalArgumentException if the iterable is null or empty
	 */
	public static <T> T joinFrom(Iterable<T> iterable, String separator) {
		return aggregateFrom(iterable, new Concat(separator));
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, Person.class).getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param clazz The class proxied by the returned object
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 */
	public static <T> T joinFrom(Iterable<T> iterable, Class<?> clazz) {
		return aggregateFrom(iterable, clazz, CONCAT);
	}

	/**
	 * Returns a lambda function defined as:
	 * <p/>
	 * 		joinFrom : (concat, iterable) => lambda : (convert : object => object) => string
	 * <p/>
	 * It is then possibly to curry this function by selecting the convert function that defines of each item must be converted in a String.
	 * This is done by invoking on that returned object the method that returns the values of the property to be summed as in the following example
	 * <p/>
	 * <code>
	 * 		String names = joinFrom(persons, Person.class, " - ").getFirstName();
	 * </code>
	 * <p/>
	 * The actual class of T is inferred from the class of the first iterable's item, but you can
	 * specify a particular class by using the overloaded method.
	 * This overloaded version should be always used when it is not insured that the given iterable is null or empty.
	 * @param iterable The iterable of the objects to containing the property to be joined.
	 * @param clazz The class proxied by the returned object
	 * @param separator The String used to separe the Strings produced by this lambda function
	 * @return A proxy of the class of the first object in the iterable representing a join lambda function
	 */
	public static <T> T joinFrom(Iterable<T> iterable, Class<?> clazz, String separator) {
		return aggregateFrom(iterable, clazz, new Concat(separator));
	}

	/**
	 * Joins all the object in the given iterable by concatenating all their String representation.
	 * It invokes toString() an all the objects and concatening them using the default separator ", ". 
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be joined
	 * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
	 */
	public static String join(Object iterable) {
		return join(iterable, ", ");
	}
	
	/**
	 * Joins all the object in the given iterable by concatenating all their String representation.
	 * It invokes toString() an all the objects and concatening them using the given separator. 
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be joined
	 * @param separator The String used to separe the item's String representation
	 * @return The concatenation of the String representation of all the objects in the given iterable or an empty String if the iterable is null or empty
	 */
	public static String join(Object iterable, String separator) {
        if (iterable == null) return "";
        try {
            return (String)aggregate(iterable, new Concat(separator));
        } catch (IllegalArgumentException e) { return iterable.toString(); }
    }

	// ////////////////////////////////////////////////////////////////////////
	// /// Conversion
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Converts all the object in the iterable using the given {@link Converter}.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param converter The converter that specifies how each object in the iterable must be converted
	 * @return A list containing all the objects in the given iterable converted using the given {@link Converter}
	 */
	public static <F, T> List<T> convert(Object iterable, Converter<F, T> converter) {
		List<T> collected = new LinkedList<T>();
		for (Iterator<T> i = convertIterator(iterable, converter); i.hasNext();) { collected.add(i.next()); }
		return collected;
	}

    /**
     * Converts all the object in the iterable using the given {@link Converter}.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable containing the objects to be converted
     * @param converter The converter that specifies how each object in the iterable must be converted
     * @return An Iterator on all the objects in the given iterable converted using the given {@link Converter}
     */
    public static <F, T> Iterator<T> convertIterator(Object iterable, Converter<F, T> converter) {
        return new ConverterIterator(converter, asIterator(iterable));
    }

    /**
	 * Converts all the values in the map using the given {@link Converter}.
	 * @param map The map containing the values to be converted
     * @param converter The converter that specifies how each map's value must be converted
	 * @return A Map containing the same keys of the original one and the value converted from the ones 
     *      in the corresponding entry of the map
	 */
    public static <K, F, T> Map<K, T> convertMap(Map<K, F> map, Converter<F, T> converter) {
        Map<K, T> converted = new HashMap<K, T>();
        for (Map.Entry<K, F> entry : map.entrySet()) { converted.put(entry.getKey(), converter.convert(entry.getValue())); }
        return converted;
    }

    /**
	 * Converts all the values in the map extracting the property defined by the given argument.
	 * @param map The map containing the values to be converted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A Map containing the same keys of the original one and the argument's value extracted from the value
     *      in the corresponding entry of the map
	 */
    public static <K, F, T> Map<K, T> convertMap(Map<K, F> map, T argument) {
        return convertMap(map, new ArgumentConverter<F, T>(argument));
    }

    /**
	 * Converts all the object in the iterable extracting the property defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method 
	 * @return A list containing the argument's value extracted from the object in the given iterable
	 */
	public static <F, T> List<T> extract(Object iterable, T argument) {
		return convert(iterable, new ArgumentConverter<F, T>(argument));
	}
	
    /**
     * Converts all the object in the iterable extracting the property defined by the given argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Unlike the method {@link #extract(Object, Object)} this one doesn't build a new collection, and the
	 * extraction is done only when someone iterates over the returned iterator.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable containing the objects to be converted
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A list containing the argument's value extracted from the object in the given iterable
     */
    public static <F, T> Iterator<T> extractIterator(Object iterable, T argument) {
        return convertIterator(iterable, new ArgumentConverter<F, T>(argument));
    }
    
	/**
	 * Converts all the object in the iterable in its String representation.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted in strings
	 * @return A list containing the String representation of the objects in the given iterable
	 */
	public static List<String> extractString(Object iterable) {
		return convert(iterable, new DefaultStringConverter());
	}
	
	/**
	 * Converts all the object in the iterable extracting the named property.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be converted
	 * @param propertyName The name of the item's property on which the item must have no duplicated value
	 * @return A list containing the property's value extracted from the object in the given iterable
	 */
	public static <F, T> List<T> extractProperty(Object iterable, String propertyName) {
		return convert(iterable, new PropertyExtractor<F, T>(propertyName));
	}
	
	/**
	 * Maps the objects in the given iterable on the value extracted using the given {@link Converter}.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be mapped
	 * @param converter The converter that specifies the key on which each object should be mapped
	 * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
	 */
	public static <F, T> Map<T, F> map(Object iterable, Converter<F, T> converter) {
		Map<T, F> map = new HashMap<T, F>();
        Iterator<F> i = (Iterator<F>)asIterator(iterable);
        while (i.hasNext()) {
            F item = i.next();
            map.put(converter.convert(item), item);
        }
		return map;
	}

	/**
	 * Indexes the objects in the given iterable based on the value of their argument.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
	 * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
	 * @param iterable The iterable containing the objects to be indexed
	 * @param argument An argument defined using the {@link Lambda#on(Class)} method
	 * @return A map having as keys the argument value extracted from the objects in the given iterable and as values the corresponding objects
	 */
	public static <F, T> Map<T, F> index(Object iterable, T argument) {
		return map(iterable, new ArgumentConverter<F, T>(argument));
	}

     /**
      * Converts the objects in the given iterable in objects of the given target Class.
      * The objects are created by invoking its constructor passing to it the values taken
      * from the object to be converted using the given arguments.
      * Actually it handles also Maps, Arrays and Iterator by collecting their values.
      * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
      * @param iterable The iterable containing the objects to be projected
      * @param targetClass The class in which the objects in the given iterable must be converted
      * @param arguments The arguments of the objects to be converted that will be used to create the objects of the target class
      * @return A list of map where each map is the result of the projection of an object in the iterable
      */
    public static <T> List<T> project(Object iterable, Class<T> targetClass, Object... arguments) {
        return convert(iterable, new ConstructorArgumentConverter<Object, T>(targetClass, arguments));
    }

    /**
     * Projects the objects in the given iterable by converting each of them in a set of key/value pairs.
     * Actually it handles also Maps, Arrays and Iterator by collecting their values.
     * Note that this method accepts an Object in order to be used in conjunction with the {@link Lambda#forEach(Iterable)}.
     * @param iterable The iterable containing the objects to be projected
     * @param projectors The converters that define how each object should be projected
     * @return A list of map where each map is the result of the projection of an object in the iterable
     */
    public static <F> List<Map<String, Object>> project(Object iterable, Converter<F, Map.Entry<String, Object>>... projectors) {
        return convert(iterable, new ProjectConverter<F>(projectors));
    }

    /**
     * Creates a converter that projects the value of the argument of an object using as alias
     * the argument property name as defined by {@link Argument#getInkvokedPropertyName()}
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A converter that can be used as projector in the {@link Lambda#project(Object, Converter[])} method
     */
    public static <F> Converter<F, Map.Entry<String, Object>> as(Object argument) {
        return new AliasedArgumentConverter<F, Object>(argument);
    }

    /**
     * Creates a converter that projects the value of the argument of an object using as the given alias
     * @param alias The key on which the argument value is paired
     * @param argument An argument defined using the {@link Lambda#on(Class)} method
     * @return A converter that can be used as projector in the {@link Lambda#project(Object, Converter[])} method
     */
    public static <F> Converter<F, Map.Entry<String, Object>> as(String alias, Object argument) {
          return new AliasedArgumentConverter<F, Object>(alias, argument);
    }

	// ////////////////////////////////////////////////////////////////////////
	// /// Matcher
	// ////////////////////////////////////////////////////////////////////////

    /**
     * Creates an hamcrest matcher that is evalued to true accordingly to the value of the passed argument
     * @param argument The boolean argument defined using the {@link Lambda#on(Class)} method that has to be matched
     * @return The hamcrest matcher that is evalued to true accordingly to the value of the passed argument
     */
	public static <T> HasArgumentWithValue<T, Boolean> having(Boolean argument) {
    	return havingValue(argument);
    }
    
    /**
     * Creates an hamcrest matcher that is evalued to true if the value of the given argument satisfies
     * the condition defined by the passed matcher.
     * @param argument The argument defined using the {@link Lambda#on(Class)} method that has to be matched
     * @param matcher The matcher against which the value of the given argument has to be compared
     * @return The hamcrest matcher that is evalued to true if the value of the passed argument matches the given matcher
     */
    public static <T, A> HasArgumentWithValue<T, A> having(A argument, Matcher<?> matcher) {
    	return havingValue(argument, matcher);
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Group
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Creates a GroupCondition that allows to group items based on the value they have on a given argument
     * @param argument The argument defined using the {@link ch.lambdaj.Lambda#on(Class)} method on which the items have to be grouped
     * @return A GroupCondition that can be used to group items through the {@link Groups#group(Iterable, ch.lambdaj.group.GroupCondition[])} method
     */
    public static <T> ArgumentGroupCondition<T> by(T argument) {
		return Groups.by(argument);
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the given grouping conditions
     * @param iterable The items to be grouped
     * @param conditions The conditions that define how the items have to be grouped
     * @return The items grouped by the given conditions
     */
	public static <T> Group<T> group(Iterable<T> iterable, Collection<? extends GroupCondition<?>> conditions) {
		return Groups.group(iterable, conditions);
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the values they have on the named JavaBean proprties
     * @param iterable The items to be grouped
     * @param groupers The names of the properties on which the items have to be grouped
     * @return The items grouped on the values of their JavaBean properties
     */
	public static <T> Group<T> group(Iterable<T> iterable, String... groupers) {
		return Groups.group(iterable, groupers);
	}

    /**
     * Organizes the given list of items in (hierarchy of) groups based on the given grouping conditions
     * @param iterable The items to be grouped
     * @param conditions The conditions that define how the items have to be grouped
     * @return The items grouped by the given conditions
     */
	public static <T> Group<T> group(Iterable<T> iterable, GroupCondition<?>... conditions) {
        return Groups.group(iterable, conditions);
    }

	// ////////////////////////////////////////////////////////////////////////
	// /// Closure
	// ////////////////////////////////////////////////////////////////////////
	
    /**
     * Binds an object to the active closure that is the last one created in the current thread.
     * @param closed The object that has to be bound to the active closure
     * @return A proxy of the same class of the passed object used to register all the invocation on the closed object
     */
	public static <T> T of(T closed) {
		return of(closed, (Class<T>)closed.getClass());
	}

    /**
     * Binds a free variable of the given class to the active closure that is the last one created in the current thread.
     * @param closedClass The type of the free variable to be bound to the active closure
     * @return A proxy of the same class of the passed class used to register all the invocation on the closed object
     */
    public static <T> T of(Class<T> closedClass) {
        return bindClosure(closedClass, closedClass);
    }

    /**
     * Binds an object to the active closure that is the last one created in the current thread.
     * @param closed The object that has to be bound to the active closure
     * @param closedClass The actual class of the proxied object
     * @return An instance of the closedClass that is actually a proxy used to register all the invocation on the closed object
     */
	public static <T> T of(T closed, Class<T> closedClass) {
		return bindClosure(closed, closedClass);
	}
	
    /**
     * Defines a free variable of the given Class for the currently active closure
     * @param clazz The Class of the new argument
     * @return A placeholder that represent a free closure variable of the given Class
     */
	public static <T> T var(Class<T> clazz) {
		return createClosureVarPlaceholder(clazz);
	}
	
    /**
     * Creates a generic (not typed) closure and binds it to the current thread
     * @return The newly created closure
     */
	public static Closure closure() {
		return createClosure();
	}
	
    /**
     * Creates a closure with a single free variable and binds it to the current thread
     * @param type1 The type of the free parameter of the newly created closure
     * @return The newly created closure
     */
	public static <A> Closure1<A> closure(Class<A> type1) {
		return createClosure(type1);
	}
	
    /**
     * Creates a closure with two free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B> Closure2<A, B> closure(Class<A> type1, Class<B> type2) {
		return createClosure(type1, type2);
	}
	
    /**
     * Creates a closure with three free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @param type3 The type of the third free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B, C> Closure3<A, B, C> closure(Class<A> type1, Class<B> type2, Class<C> type3) {
		return createClosure(type1, type2, type3);
	}

    /**
     * Creates a closure with four free variables and binds it to the current thread
     * @param type1 The type of the first free variable of the newly created closure
     * @param type2 The type of the second free variable of the newly created closure
     * @param type3 The type of the third free variable of the newly created closure
     * @param type4 The type of the fourth free variable of the newly created closure
     * @return The newly created closure
     */
	public static <A, B, C, D> Closure4<A, B, C, D> closure(Class<A> type1, Class<B> type2, Class<C> type3, Class<D> type4) {
		return createClosure(type1, type2, type3, type4);
	}

    /**
     * Returns the result of the invocation of the method that uses the given delayed closure
     * @param delayedClosure The closure defined through the {@link DelayedClosure} syntax
     * @return The result of the invocation of the method that uses the given delayed closure
     */
    public static <T> ClosureResult<T> delayedClosure(DelayedClosure<T> delayedClosure) {
        return delayedClosure.getClosureResult();
    }
}