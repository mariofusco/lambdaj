package ch.lambdaj.util.iterator;

import org.junit.*;
import static org.junit.Assert.*;

import static java.util.Arrays.asList;
import java.util.*;

/**
 * @author Mario Fusco
 */
public class IteratorFactoryTest {

    @Test
    public void testInvalidIterator() {
        try {
            IteratorFactory.asIterator("xxx");
            fail("asIterator on a non-collection must fail");
        } catch (IllegalArgumentException iae) { }
        try {
            IteratorFactory.asResettableIterator("xxx");
            fail("asResettableIterator on a non-collection must fail");
        } catch (IllegalArgumentException iae) { }
    }

    @Test
    public void testResettableIteratorOnIterator() {
        ResettableIterator<String> iterator = (ResettableIterator<String>)IteratorFactory.asResettableIterator(asList("one", "two", "three").iterator());
        try {
            iterator.remove();
            fail("remove invocation on resattable iterator must fail");
        } catch (UnsupportedOperationException uoe) { }

        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        iterator.reset();
        assertEquals("one", iterator.next());
        assertEquals("two", iterator.next());
        assertEquals("three", iterator.next());

        try {
            iterator.next();
            fail("next invocation after last item must fail");
        } catch (NoSuchElementException nse) { }
    }
}
