package ch.lambdaj.function.matcher;

import org.hamcrest.*;

/**
 * A type safe LambdaJMatcher.
 * @author Mario Fusco
 */
public abstract class Predicate<T> extends LambdaJMatcher<T> {

    private final InnerMatcher innerMatcher = new InnerMatcher();

    /**
     * Evaluates this Predicate on the given item
     * @param item The object against which this Predicate is evaluated
     * @return true if item matches, otherwise false
     */
    public abstract boolean apply(T item);

    /**
     * {@inheritDoc}
     */
    public final boolean matches(Object item) {
        return innerMatcher.matches(item);
    }

    private class InnerMatcher extends TypeSafeMatcher<T> {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean matchesSafely(T item) {
            return apply(item);
        }

        /**
         * {@inheritDoc}
         */
        public void describeTo(Description description) {}
    }
}
