// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import ch.lambdaj.group.*;
import ch.lambdaj.mock.*;
import org.junit.*;

import java.util.*;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Strings.json;
import static ch.lambdaj.collection.LambdaCollections.with;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * @author Mario Fusco
 */
public class LambdaGroupTest {
    private final Person me = new Person("Mario", "Fusco", 35);
    private final Person luca = new Person("Luca", "Marrocco", 29);
    private final Person biagio = new Person("Biagio", "Beatrice", 39);
    private final Person celestino = new Person("Celestino", "Bellone", 29);

    @Test
    public void testGroupByAge() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        LambdaGroup<Person> group = meAndMyFriends.group(by(on(Person.class).getAge()));
        assertFalse(group.isLeaf());
        assertEquals(4, group.getSize());
        assertEquals(3, group.subgroups().size());

        Set<String> keys = group.keySet();
        assertTrue(keys.contains("29"));
        assertTrue(keys.contains("35"));
        assertTrue(keys.contains("39"));

        assertTrue(group.findAll().contains(me));
        assertTrue(group.findAll().contains(biagio));
        assertTrue(group.findAll().contains(luca));
        assertTrue(group.findAll().contains(celestino));

        LambdaGroup<Person> group29aged = group.findGroup(29);
        assertTrue(group29aged.isLeaf());
        assertEquals(2, group29aged.getSize());
        assertEquals(0, group29aged.keySet().size());
        assertEquals(0, group29aged.subgroups().size());

        Collection<Person> persons29Aged = group29aged.findAll();
        assertTrue(persons29Aged.contains(luca));
        assertTrue(persons29Aged.contains(celestino));
    }

    @Test
    public void testGroupByAgeAsPersonsWithName() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        LambdaGroup<Person> group = meAndMyFriends.group(by(on(Person.class).getAge()).as("persons").head(on(Person.class).getFirstName(), "name"));
        assertEquals(0, group.getHeads().size());
        assertEquals("", group.getHeadValue("name"));

        LambdaGroup<Person> group35aged = group.findGroup("35");
        assertEquals(2, group35aged.getHeads().size());
        assertTrue(group35aged.getHeads().contains("age"));
        assertEquals("35", group35aged.getHeadValue("age"));
        assertTrue(group35aged.getHeads().contains("name"));
        assertEquals("Mario", group35aged.getHeadValue("name"));
    }

    @Test
    public void testGroupByAgeAndName() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        LambdaGroup<Person> group = meAndMyFriends.group(by(on(Person.class).getAge()), by(on(Person.class).getFirstName()));

        LambdaGroup<Person> group29aged = group.findGroup(29);
        assertFalse(group29aged.isLeaf());

        Collection<Person> persons29Aged = group29aged.findAll();
        assertTrue(persons29Aged.contains(luca));
        assertTrue(persons29Aged.contains(celestino));

        Group<Person> groupLuca = group29aged.findGroup("Luca");
        assertTrue(groupLuca.findAll().contains(luca));
    }

    @Test
    public void testGroupAndSubgroup() {
        LambdaList<Person> meAndMyFriends = with(me, luca, biagio, celestino);

        LambdaGroup<Person> group = meAndMyFriends.group(by(on(Person.class).getAge()), by(on(Person.class).getFirstName()));
        assertEquals(4, group.getSize());
        assertEquals(3, group.subgroups().size());
        assertFalse(group.isLeaf());
        assertEquals(me, group.first());
        assertNull(group.key());

        Group<Person> subgroup = group.subgroups().get(0);
        assertFalse(subgroup.isLeaf());
        assertEquals(me, subgroup.first());
        assertEquals(me.getAge(), subgroup.key());

        Group<Person> subsubgroup = subgroup.subgroups().get(0);
        assertTrue(subsubgroup.isLeaf());
        assertEquals(me, subgroup.first());
        assertEquals(me.getFirstName(), subsubgroup.key());
    }
}
