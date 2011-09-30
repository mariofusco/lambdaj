package ch.lambdaj;


import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static ch.lambdaj.Lambda.*;

import ch.lambdaj.function.closure.*;

import java.util.*;
import java.lang.reflect.*;

import org.junit.*;

/**
 * @author Mario Fusco
 */
public class ClosureSpecialCasesTest {

    void oneArg(Object o) {
        System.out.println("called one-arg: " + o);
    }

    void twoArg(Object o, String readThis) {
        System.out.println("called two-arg: " + o + ", " + readThis);
    }

    <T> T createObject(Class<T> clazz, String arg) {
        try {
            return clazz.getConstructor(String.class).newInstance(arg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testWithEmptyListAndUnboundVar() {
        List<String> items = new ArrayList<String>();
        Closure closure = closure(); {
            of(this).twoArg(items, var(String.class));
        }
        items.add("added later");
        closure.apply("Read this");
        assertEquals(asList("added later"), items);
    }

    @Test
    public void testWithNonEmptyListAndUnboundVar() {
        List<String> items = new ArrayList(asList("initial"));
        Closure closure = closure(); {
            of(this).twoArg(items, var(String.class));
        }
        items.add("added later");
        closure.apply("Read this");
        assertEquals(asList("initial", "added later"), items);
    }

    @Test
    public void testWithEmptyListOnly() {
        List<String> items = new ArrayList();
        Closure closure = closure(); {
            of(this).oneArg(items);
        }
        items.add("added later");
        closure.apply();
        assertEquals(asList("added later"), items);
    }

    @Test
    public void testWithNonEmptyListOnly() {
        List<String> items = new ArrayList(asList("initial"));
        Closure closure = closure(); {
            of(this).oneArg(items);
        }
        items.add("added later");
        closure.apply();
        assertEquals(asList("initial", "added later"), items);
    }

    @Test
    public void testWithFixedClassArgument() {
        Closure objectCreator = closure(); {
            of(this).createObject(String.class, var(String.class));
        }
        String string = (String)objectCreator.apply("pippo");
        assertEquals("pippo", string);
    }

    @Test
    public void testWithFreeClassArgument() {
        Closure objectCreator = closure(); {
            of(this).createObject(var(Class.class), var(String.class));
        }
        String string = (String)objectCreator.apply(String.class, "pippo");
        assertEquals("pippo", string);
    }
}
