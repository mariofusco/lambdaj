package ch.lambdaj;

import static org.junit.Assert.*;
import static ch.lambdaj.Lambda.*;

import ch.lambdaj.function.closure.*;
import java.io.*;
import java.util.*;
import org.junit.*;

/**
 * @author Guillaume Bort
 * @author Mario Fusco
 */
public class DelayedClosureTest {

    PrintWriter out;

    @Test
    public void testWithTransaction() {
        StringWriter sw = new StringWriter();
        out = new PrintWriter(sw);

        withTransaction(); {
            of(this).sayHello();
        }

        System.out.println(sw.toString());
        assertEquals("BEGIN -- Hello ! -- COMMIT", sw.toString());
    }

    @Test
    public void testMyGrep() throws Exception {
        List<String> strings = new ArrayList<String>();
        strings.add("Yop");
        strings.add("Ho");
        strings.add("Kiki");
        strings.add("Bi");

        ClosureResult<List<String>> result = grep(strings); {
            of(this).moreThan2Chars(var(String.class));
        }

        List<String> resultList = result.get();
        assertEquals(2, resultList.size());
        assertEquals("Yop", resultList.get(0));
        assertEquals("Kiki", resultList.get(1));
    }

    // ~~~~

    public boolean moreThan2Chars(String c) {
        return c.length() > 2;
    }

    public void sayHello() {
        out.print("Hello !");
    }

    public void withTransaction() {
        delayedClosure(new DelayedClosure<Void>() {

            @Override
            public Void doWithClosure(Closure closure) {
                out.print("BEGIN -- ");
                closure.apply();
                out.print(" -- COMMIT");
                return null;
            }
        });
    }

    public <T> ClosureResult<List<T>> grep(final List<T> objects) {
        return delayedClosure(new DelayedClosure<List<T>>() {

            @Override
            public List<T> doWithClosure(Closure closure) {
                List<T> filtered = new ArrayList<T>();
                for (T object : objects) {
                    if ((Boolean) closure.apply(object)) {
                        filtered.add(object);
                    }
                }
                return filtered;
            }
        });
    }
}
