// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import org.hamcrest.*;
import static org.hamcrest.Matchers.equalTo;

import java.util.*;

/**
 * Enriches the features of the native java switch.
 * It can be effectively used to implement strategy or factory patterns.
 * @author Mario Fusco
 */
public class Switcher<T> {

    private final List<Case<T>> cases = new ArrayList<Case<T>>();
    private Result<T> defaultResult;

    /**
     * Adds a case that invokes the given closure when this switcher is executed on an object equal to the given one.
     * @param object The object to be matched
     * @param closure The closure invoked when this case is executed
     * @return The switcher itself
     */
    public Switcher<T> addCase(Object object, AbstractClosure closure) {
        return addCase(equalTo(object), closure);
    }

    /**
     * Adds a case that invokes the given closure when this switcher is executed on an object that matches the given Matcher.
     * @param matcher The matcher used to activate this case
     * @param closure The closure invoked when this case is executed
     * @return The switcher itself
     */
    public Switcher<T> addCase(Matcher<?> matcher, AbstractClosure closure) {
        cases.add(new Case<T>(matcher, new ClosureResult<T>(closure)));
        return this;
    }

    /**
     * Adds a case that returns the given result when this switcher is executed on an object equal to the given one.
     * @param object The object to be matched
     * @param result The value returned when this case is executed
     * @return The switcher itself
     */
    public Switcher<T> addCase(Object object, T result) {
        return addCase(equalTo(object), result);
    }

    /**
     * Adds a case that returns the given result when this switcher is executed on an object that matches the given Matcher.
     * @param matcher The matcher used to activate this case
     * @param result The value returned when this case is executed
     * @return The switcher itself
     */
    public Switcher<T> addCase(Matcher<?> matcher, T result) {
        cases.add(new Case<T>(matcher, new FixedResult<T>(result)));
        return this;
    }

    /**
     * Invokes the given closure when no other case matches during the invocation of this switcher.
     * @param closure The closure invoked when no other case matches
     * @return The switcher itself
     */
    public Switcher<T> setDefault(AbstractClosure closure) {
        defaultResult = new ClosureResult<T>(closure);
        return this;
    }

    /**
     * Returns the given object when no other case matches during the invocation of this switcher.
     * @param defaultResult The value returned when when no other case matches
     * @return The switcher itself
     */
    public Switcher<T> setDefault(T defaultResult) {
        this.defaultResult = new FixedResult<T>(defaultResult);
        return this;
    }

    /**
     * Executes with the given args the first case that matches the given match or the defaut one
     * if it has been defined and no other case matches.
     * @param match The object to be matched to decide witch case has to be executed
     * @param args The arguments used to invoke the matching case
     * @return The result of the matching case invocation
     */
    public T exec(Object match, Object ... args) {
        for (Case<T> c : cases) { if (c.matches(match)) return c.exec(args); }
        return defaultResult != null ? defaultResult.exec(args) : null;
    }

    /**
     * Executes with the given args all the cases matching the given match plus the defaut one
     * if it has been defined.
     * @param match The object to be matched to decide witch case has to be executed
     * @param args The arguments used to invoke the matching cases
     * @return The results of all the matching case invocations
     */
    public List<T> execAll(Object match, Object ... args) {
        List<T> results = new ArrayList<T>();
        for (Case<T> c : cases) { if (c.matches(match)) results.add(c.exec(args)); }
        if (defaultResult != null) results.add(defaultResult.exec(args));
        return results;
    }

    private static final class Case<T> {
        private final Matcher<?> matcher;
        private final Result<T> result;

        private Case(Matcher<?> matcher, Result<T> result) {
            this.matcher = matcher;
            this.result = result;
        }

        private boolean matches(Object match) {
            return matcher.matches(match);
        }

        private T exec(Object ... args) {
            return result.exec(args);
        }
    }

    private interface Result<T> {
        /**
         * Evaluates a given matching case with the given args
         * @param args The args used to evaluate the case
         * @return The evaluation result
         */
        T exec(Object ... args);
    }

    private static final class FixedResult<T> implements Result<T> {
        private final T value;

        private FixedResult(T value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        public T exec(Object ... args) {
            return value;
        }
    }

    private static final class ClosureResult<T> implements Result<T> {
        private final AbstractClosure closure;

        private ClosureResult(AbstractClosure closure) {
            this.closure = closure;
        }

        /**
         * {@inheritDoc}
         */
        public T exec(Object ... args) {
            return (T)closure.closeOne(args);
        }
    }
}
