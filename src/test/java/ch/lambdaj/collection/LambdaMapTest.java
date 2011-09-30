// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.collection;

import ch.lambdaj.mock.*;
import org.junit.*;

import java.util.*;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.collection.LambdaCollections.with;
import static junit.framework.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Mario Fusco
 */
public class LambdaMapTest {

    private final Person me = new Person("Mario", "Fusco", 35);
    private final Person luca = new Person("Luca", "Marrocco", 29);
    private final Person biagio = new Person("Biagio", "Beatrice", 39);
    private final Person celestino = new Person("Celestino", "Bellone", 29);

    @Test
    public void testRetainKeys() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getFirstName());
        map = map.retainKeys(startsWith("M"));
        assertEquals(1, map.size());
        assertEquals(me, map.get("Mario"));
        assertNull(map.get("Luca"));
        assertEquals(map.toString(), "{Mario=Mario Fusco}");
    }

    @Test
    public void testRemoveKeys() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getLastName());
        map = map.removeKeys(startsWith("B"));
        assertEquals(2, map.size());
        assertEquals(me, map.get("Fusco"));
        assertNull(map.get("Beatrice"));
    }

    @Test
    public void testRetainValues() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getFirstName());
        map = map.retainValues(having(on(Person.class).getAge(), equalTo(29)));
        assertEquals(2, map.size());
        assertNull(map.get("Mario"));
        assertEquals(luca, map.get("Luca"));
    }

    @Test
    public void testRemoveValues() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getFirstName());
        map = map.removeValues(having(on(Person.class).getAge(), equalTo(29)));
        assertEquals(2, map.size());
        assertEquals(me, map.get("Mario"));
        assertNull(map.get("Luca"));
    }

    @Test
    public void testChangeMapValues() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getFirstName());
        Collection<Person> values = map.values();

        Collection<String> extract = with(values)
            .retain(having(on(Person.class).getAge(), equalTo(29)))
            .extract(on(Person.class).getLastName());

          assertEquals(2, extract.size());
    }

    @Test
    public void testMap() {
        LambdaMap<String, Person> map = with(me, luca, biagio, celestino).index(on(Person.class).getFirstName());
        LambdaMap<String, Person> map2 = map.clone();
        assertEquals(map, map2);

        assertTrue(map.equals(map));
        assertEquals(4, map.entrySet().size());
        map.hashCode();
        assertTrue(map.containsKey("Mario"));
        assertTrue(map.containsValue(me));

        map.remove("Luca");
        assertEquals(3, map.size());
        assertEquals(3, map.keySet().size());
        map.put("Luca", luca);
        assertEquals(4, map.size());
        assertEquals(4, map.values().size());

        Map<String, Person> other = new HashMap<String, Person>();
        other.put("Gianfranco", new Person("Gianfranco", "Tognana", 40));
        map.putAll(other);
        assertEquals(5, map.size());
        assertNotNull(map.get("Gianfranco"));

        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testNonCloneableMap() {
        Map<String, String> map = new NonCloneableMap<String, String>();
        map.put("Italy", "Rome");
        map.put("Germany", "Berlin");

        Map<String, String> clonedMap = with(map).clone();
        assertEquals(map, clonedMap);
    }

    public static class NonCloneableMap<K, v> extends HashMap<K, v> {
        @Override
        public Object clone() {
            throw new RuntimeException();
        }
    }
}
