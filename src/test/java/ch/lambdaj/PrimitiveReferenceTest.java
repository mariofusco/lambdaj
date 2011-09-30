// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author f.baronti
 */
public class PrimitiveReferenceTest {

    private static final int ITEMS = 100;
    private static final int TESTS = 1000;

    private List<TestItem> items;

    @Before
    public void setUp() {
        items = new ArrayList<TestItem>();
        for (int i = 0; i < ITEMS; i++) {
            items.add(new TestItem());
        }
    }

    @Test
    public void testStressParameterlessFilter() throws InterruptedException {
        for (int t = 0; t < TESTS; t++) {
            List<TestItem> l = filter(having(on(TestItem.class).isValue(), equalTo(true)), items);
            assertEquals("Mismatch at iteration " + t + " on parameterless filter,", ITEMS, l.size());
        }
    }

    @Test
    public void testStressPrimitiveParameterFilter() throws InterruptedException {
        for (int t = 0; t < TESTS; t++) {
            List<TestItem> l = filter(having(on(TestItem.class).isValue(0), equalTo(true)), items);
            assertEquals("Mismatch at iteration " + t + " on parameter filter,", ITEMS, l.size());
        }
    }

    @Test
    public void testStressParameterFilter() throws InterruptedException {
        for (int t = 0; t < TESTS; t++) {
            List<TestItem> l = filter(having(on(TestItem.class).isValue(0L), equalTo(true)), items);
            assertEquals("Mismatch at iteration " + t + " on parameter filter,", ITEMS, l.size());
        }
    }

    public static class TestItem {
        public boolean isValue(Long i) {
            return true;
        }

        public boolean isValue(int i) {
            return true;
        }

        public boolean isValue() {
            return true;
        }
    }
}

