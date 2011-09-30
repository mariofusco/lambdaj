package ch.lambdaj;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.asList;
import static junit.framework.Assert.*;

import ch.lambdaj.function.closure.*;
import org.junit.*;

import java.util.*;

public class ClosureVoidMethodInvocationTest {

    class Person {
        private final String name;

        Person(String name) {
            this.name = name;
        }

        void talk() {
            talked.add(name);
        }

        Person getFriend(String friendName) {
            return new Person(friendName);
        }
    }

    /* capture fact that method was called */
    private List<String> talked = new ArrayList<String>();

    @Test
    public void testShallReplayChainedMethodCalls() {
        Closure closure = closure(); {
            of(new Person("Bob")).getFriend("Sue").talk();
        }

        assertTrue(talked.isEmpty());
        closure.apply();
        assertEquals(asList("Sue"), talked);
    }
}
