// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import org.junit.*;
import ch.lambdaj.mock.*;
import static ch.lambdaj.collection.LambdaCollections.*;
import static ch.lambdaj.Lambda.*;
import ch.lambdaj.function.convert.*;
import ch.lambdaj.function.aggregate.*;
import static ch.lambdaj.function.aggregate.Money.money;

import java.util.*;
import static java.util.Arrays.asList;

import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.greaterThan;

/**
 * @author Mario Fusco
 */
public class LambdaIteratorTest {

    @Test
    public void test1() {
        String result = with(asList(1, 2, 3, 4, 5).iterator()).join("!");
        assertNotNull(result);
        assertEquals("1!2!3!4!5", result);
    }

    Person me = new Person("Mario", "Fusco", 35);
    Person luca = new Person("Luca", "Marrocco", 29);
    Person biagio = new Person("Biagio", "Beatrice", 39);
    Person celestino = new Person("Celestino", "Bellone", 29);
    List<Person> meAndMyFriends = asList(me, luca, biagio, celestino);

    @Test
    public void testConcatNull1() {
        String result = with(asList(null, "a").iterator()).join();
        assertEquals(result, "a");
    }

    @Test
    public void testConcatNull2() {
        String result = with(Arrays.<String> asList(null, null).iterator()).join();
        assertEquals(result, "");
    }

    @Test
    public void testConcatNull3() {
        String result = with(asList("a", null, "b").iterator()).join("-");
        assertEquals(result, "a-b");
    }

    @Test
    public void testMap() {
        Map<String, Person> personsByName = with(meAndMyFriends.iterator()).map(on(Person.class).getFirstName());

        assertNotNull(personsByName);
        assertEquals(4, personsByName.size());
    }

    @Test
    public void testMap1() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends.iterator()).map(on(Person.class).getAge());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    @Test
    public void testMap2() {
        Map<Integer, Person> personsByAge = with(meAndMyFriends.iterator()).map(on(Person.class).getAge()).convertValues(new MapConverter());

        assertNotNull(personsByAge);
        assertEquals(3, personsByAge.size());
    }

    private static class MapConverter implements Converter<Person, Person> {
        public Person convert(Person from) {
            System.out.println(from);
            return from;
        }

    }

    public static Premium premiumOf(final String value) {
        return new Premium() {
            public Money getPremium() {
                return money(value);
            }
        };
    }

    public interface Premium {
        Money getPremium();
    }

    public Person convert(Person from) {
        return null;
    }

    @Test
    public void testIterator() {
        LambdaIterator<Integer> iterator = with(1, 2, 3, 4, 5).iterator();

        LambdaIterator<Integer> filteredIterator = iterator.retain(greaterThan(3));
        assertEquals(4, (int)filteredIterator.next());
        assertEquals(5, (int)filteredIterator.next());
        assertFalse(filteredIterator.hasNext());

        try {
            filteredIterator.remove();
        } catch (UnsupportedOperationException e) { }
    }

    @Test
    public void testIteratorExtract() {
        LambdaIterator<Person> iterator = with(meAndMyFriends).iterator();
        Iterator<String> nameIterator = iterator.extract(on(Person.class).getFirstName());
        assertEquals("Mario", nameIterator.next());
    }
}
