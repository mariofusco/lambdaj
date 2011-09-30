// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.aggregate.Money.money;
import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.*;

import ch.lambdaj.*;
import org.hamcrest.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;

import ch.lambdaj.function.aggregate.Money;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.mock.Person;
import static ch.lambdaj.collection.LambdaCollections.with;

/**
 * @author Mario Fusco
 */
public class LambdaCollectionTest {

    @Test
    public void test1() {
        Collection<Integer> numbers = asList(1, 2, 3, 4, 5);
        Collection<Integer> result = with(numbers).retain(greaterThan(3));
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    Matcher<Integer> Odd = new TypeSafeMatcher<Integer>() {
        public boolean matchesSafely(Integer item) {
            return item % 2 == 1;
        }

        public void describeTo(Description description) {
            description.appendText("odd()");
        }
    };

    @Test
    public void test2() {
        Collection<Integer> numbers = asList(1, 2, 3, 4, 5);
        LambdaCollection<Integer> result = with(numbers).retain(Odd).append(2, 4);
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("[1, 3, 5, 2, 4]", result.toString());
    }

    Person me = new Person("Mario", "Fusco", 35);
    Person biagio = new Person("Biagio", "Beatrice", 39);
    Person celestino = new Person("Celestino", "Bellone", 29);
    Collection<Person> meAndMyFriends = asList(me, biagio, celestino);

    @Test
    public void test3() {
        Collection<Person> oldFriends = with(meAndMyFriends).retain(having(on(Person.class).getAge(), greaterThan(30)));
        assertNotNull(oldFriends);
        assertEquals(2, oldFriends.size());
    }

    @Test
    public void testConcatNull1() {
        String result = with(meAndMyFriends).join();
        assertEquals(result, "Mario Fusco, Biagio Beatrice, Celestino Bellone");
    }

    @Test
    public void testMap() {
        Map<String, Person> personsByName = with(meAndMyFriends).map(on(Person.class).getFirstName());

        assertNotNull(personsByName);
        assertEquals(3, personsByName.size());
    }

    @Test
    public void testMap1() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    @Test
    public void testMap2() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends).map(on(Person.class).getAge()).convertValues(new MapConverter());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    private static class MapConverter implements Converter<Person, Person> {
        public Person convert(Person from) {
        System.out.println(from);
            return from;
        }
    }

    @Test
    public void testCollection() {
        Collection<Integer> c = with(1, 2, 3, 4, 5);
        LambdaCollection<Integer> collection = with(c);
        assertEquals(5, collection.size());
        assertFalse(collection.isEmpty());
        assertTrue(collection.containsAll(with(4, 3, 2)));
        assertEquals(collection, collection.clone());

        collection.add(6);
        assertEquals(6, collection.size());

        collection.clear();
        assertEquals(0, collection.size());
        assertTrue(collection.isEmpty());

        collection.addAll(with(1, 2, 3, 4, 5));
        assertEquals(5, collection.size());
        collection.remove(6);
        assertEquals(5, collection.size());
        collection.remove(5);
        assertEquals(4, collection.size());
        collection.removeAll(with(1, 2));
        assertEquals(2, collection.size());
        collection.retainAll(with(2, 3));
        assertEquals(1, collection.size());
        assertEquals(3, (int)collection.iterator().next());

        Object[] array = collection.toArray();
        assertEquals(1, array.length);
        assertEquals(3, array[0]);

        Integer[] typedArray = collection.toArray(new Integer[1]);
        assertEquals(1, typedArray.length);
        assertEquals(3, (int)typedArray[0]);
   }
}
