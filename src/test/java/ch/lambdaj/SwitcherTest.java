// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import org.junit.*;
import static org.junit.Assert.*;
import org.hamcrest.*;
import static org.hamcrest.Matchers.*;
import org.mockito.internal.matchers.*;
import ch.lambdaj.function.closure.*;
import static ch.lambdaj.Lambda.*;
import ch.lambdaj.mock.*;
import static junit.framework.Assert.assertNull;

import java.util.*;

/**
 * @author Mario Fusco
 */
public class SwitcherTest {

    public Integer add(Integer val1, Integer val2) {
        return val1 + val2;
    }
    public Integer sub(Integer val1, Integer val2) {
        return val1 - val2;
    }
    public Integer mul(Integer val1, Integer val2) {
        return val1 * val2;
    }
    public Integer div(Integer val1, Integer val2) {
        return val1 / val2;
    }

    @Test
    public void testExecSwitch() {
        Switcher<Integer> switcher = new Switcher<Integer>()
                .addCase("+", new Closure() {{ of(SwitcherTest.this).add(var(Integer.class), var(Integer.class)); }})
                .addCase("-", closure().of(this, "sub", var(Integer.class), var(Integer.class)))
                .addCase("*", closure().of(this, "mul", var(Integer.class), var(Integer.class)))
                .addCase("/", closure().of(this, "div", var(Integer.class), var(Integer.class)));

        assertTrue(9 == switcher.exec("+", 6, 3));
        assertTrue(3 == switcher.exec("-", 6, 3));
        assertTrue(18 == switcher.exec("*", 6, 3));
        assertTrue(2 == switcher.exec("/", 6, 3));
        assertNull(switcher.exec("?", 6, 3));

        switcher.setDefault(closure().of(this, "add", var(Integer.class), var(Integer.class)));
        assertTrue(9 == switcher.exec("?", 6, 3));

        switcher.setDefault(0);
        assertTrue(0 == switcher.exec("?", 6, 3));
    }

    @Test
    public void testExecAllSwitch() {
        Switcher<Integer> switcher = new Switcher<Integer>()
                .addCase(contains("+"), closure().of(this, "add", var(Integer.class), var(Integer.class)))
                .addCase(contains("-"), closure().of(this, "sub", var(Integer.class), var(Integer.class)))
                .addCase(contains("*"), closure().of(this, "mul", var(Integer.class), var(Integer.class)))
                .addCase(contains("/"), closure().of(this, "div", var(Integer.class), var(Integer.class)));

        List<Integer> results = switcher.execAll("+*", 6, 3);
        assertEquals(2, results.size());
        assertTrue(9 == results.get(0));
        assertTrue(18 == results.get(1));
    }

    @Test
    public void testSwitchOnPersons() {
        Switcher<String> switcher = new Switcher<String>()
                .addCase(new Person("me", 35), "me")
                .addCase(having(on(Person.class).getAge(), lessThan(30)), "young")
                .addCase(having(on(Person.class).getAge(), greaterThan(70)), "old")
                .setDefault("adult");

        assertEquals("me", switcher.exec(new Person("me", 35)));
        assertEquals("adult", switcher.exec(new Person("sister", 31)));
        assertEquals("old", switcher.exec(new Person("dad", 71)));
    }

    @Test
    public void testSwitchAsFactory() {
        Switcher<Person> factory = new Switcher<Person>()
                .addCase("me", closure().of(Person.class, Closure.CONSTRUCTOR, "Mario", var(Integer.class)))
                .addCase("sis", closure().of(Person.class, Closure.CONSTRUCTOR, "Irma", var(Integer.class)))
                .addCase("dad", closure().of(Person.class, Closure.CONSTRUCTOR, "Domenico", var(Integer.class)));

        Person sis = factory.exec("sis", 31);
        assertEquals("Irma", sis.getFirstName());
        assertEquals(31, sis.getAge());

        Person dad = factory.exec("dad", 70);
        assertEquals("Domenico", dad.getFirstName());
        assertEquals(70, dad.getAge());

        try {
            factory.exec("sis", "Irma", 31);
            fail("Invocation with wrong parameter number must fail");
        } catch (WrongClosureInvocationException e) { }

        try {
            factory.setDefault(closure().of(Person.class, Closure.CONSTRUCTOR, var(Integer.class)));
            fail("Closure creation with wrong parameter number must fail");
        } catch (Exception e) { }
    }

    private static Contains contains(String contained) {
        return new Contains(contained);
    }

    private static class Contains extends BaseMatcher {

        private final String contained;

        public Contains(String contained) {
            this.contained = contained;
        }

        public boolean matches(Object o) {
            return o.toString().contains(contained);
        }

        public void describeTo(Description description) { }
    }
}
