// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.function.closure;

import static ch.lambdaj.function.closure.ClosuresFactory.*;
import static ch.lambdaj.util.IntrospectionUtil.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * The abstract class extended by all the lambdaj closures
 * @author Mario Fusco
 */
abstract class AbstractClosure {

    /**
     * The name used to identify a closure that inokes a constructor
     */
    public static final String CONSTRUCTOR = "<init>";

	Object closed;
	
	List<Invokable> invokables = new ArrayList<Invokable>();
	List<Object[]> argsList = new ArrayList<Object[]>();
	
	Object[] curriedVars;
	boolean[] curriedVarsFlags;

    int freeVarsNumber = 0;

    private final List<Object[]> unhandeledInvocations = new ArrayList<Object[]>();

    /**
     * Binds a free variable of the given class to the this closure.
     * @param closedClass The type of the free variable to be bound to this closure
     * @return A proxy of the same class of the passed class used to register all the invocation on the closed object
     */
    public <T> T of(Class<T> closedClass) {
        return of(closedClass, closedClass);
    }

    /**
     * Binds an object to this closure.
     * @param closed The object that has to be bound to this closure
     * @return A proxy of the same class of the passed object used to register all the invocation on the closed object
     */
    public <T> T of(T closed) {
        return of(closed, (Class<T>)closed.getClass());
    }

    /**
     * Binds an object to this closure.
     * @param closed The object that has to be bound to this closure
     * @param closedClass The actual class of the proxied object
     * @return An instance of the closedClass that is actually a proxy used to register all the invocation on the closed object
     */
    public <T> T of(Object closed, Class<T> closedClass) {
        setClosed(closed);
        return createProxyClosure(this, closedClass);
    }

    /**
     * Defines the method invoked by this closure.
     * @param closedObject The object on which the closure has to be invoked. It can be a fixed object or a Class.
     *                     In this last case, if the method is not static, it is treated as it was an
     *                     unbound argument defined through the {@link ch.lambdaj.Lambda#var(Class)} method
     * @param methodName The name of the method invoked by this closure or {@link AbstractClosure#CONSTRUCTOR}
     *                   if you want to call a constructor
     * @param args The arguments used to invoke this closure. They can be a mixed of fixed value and
     *             unbound one defined through the {@link ch.lambdaj.Lambda#var(Class)} method
     * @return The closure itself
     */
    protected AbstractClosure of(Object closedObject, String methodName, Object ... args) {
        Class<?> closedClass = closedObject instanceof Class<?> ? (Class<?>)closedObject : closedObject.getClass();
        Invokable invokable = methodName.equals(CONSTRUCTOR) ?
                new InvokableConstructor(findConstructor(closedClass, args)) :
                new InvokableMethod(findMethod(closedClass, methodName, args));
        bindInvocation(invokable, args);
        setClosed(invokable.isStatic() ? null : closedObject);
        return this;
    }

    /**
     * Returns the number of free variables in this closure
     * @return The number of free variables in this closure
     */
    public int getFreeVarsNumber() {
        return freeVarsNumber;
    }

    /**
     * Dynamically casts this closure to a one-method interface in order to invoke its method in a strongly typed way
     * @param asInterface The interface to which this closure should be casted
     * @return A proxy that implements the requested interface and wraps this closure
     * @throws IllegalArgumentException if the given Class is not actually an interface or if it has more than one method
     */
	@SuppressWarnings("unchecked")
	public <T> T cast(Class<T> asInterface) throws IllegalArgumentException {
		if (!asInterface.isInterface()) throw new IllegalArgumentException("Cannot cast a closure to the concrete class " + asInterface.getName());
		Method[] methods = asInterface.getMethods();
		if (methods.length != 1) throw new IllegalArgumentException("Cannot cast a closure to an interface with more than one method");

		return (T)Proxy.newProxyInstance(asInterface.getClassLoader(), new Class<?>[] { asInterface },
			new InvocationHandler() {
				public Object invoke(Object proxy, Method method, Object[] args) throws WrongClosureInvocationException {
					return closeOne(args);
				}
			}
		);
	}

    void setClosed(Object closed) {
		this.closed = closed;
        if (isClosedOnFreeVar()) freeVarsNumber++;
	}

    private boolean isClosedOnFreeVar() {
        return closed instanceof Class<?>;
    }
	
    void bindInvocation(Method method, Object[] args) {
        if (!method.isAccessible()) method.setAccessible(true);
        bindInvocation(new InvokableMethod(method),args);
    }

    private void bindInvocation(Invokable invokable, Object[] args) {
		invokables.add(invokable);
		if (args != null) for (Object arg : args) { if (getClosureVarType(arg).isClosureVarPlaceholder()) freeVarsNumber++; }
		argsList.add(args);
	}

    void closeUnhandledInvocations() {
        for (Object[] vars : unhandeledInvocations) { closeOne(vars); }
        unhandeledInvocations.clear();
    }

    /**
     * Invokes this closure once by applying the given set of variables to it.
     * @param vars The set of variables used to invoke this closure once
     * @return The result of the closure invocation
     * @throws WrongClosureInvocationException if the number of the passed variables doesn't correspond to one
     * with which this closure has been defined
     */
	Object closeOne(Object... vars) throws WrongClosureInvocationException {
        if (invokables.isEmpty()) {
            unhandeledInvocations.add(vars);
            return null;
        }

		List<Object[]> boundParams = bindParams(vars);
		Object result = isClosedOnFreeVar() ? vars[0] : closed;
		
		Iterator<Object[]> argsIterator = boundParams != null ? boundParams.iterator() : null;
		for (Invokable invokable : invokables) {
            result = invokable.invoke(result, argsIterator != null ? argsIterator.next() : null);
        }
		return result;
	}

    /**
     * Invokes this closure once for each passed variable.
     * It is then assumed that this closure has been defined with exactly one free variable
     * @param vars The set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if this closure hasn't been defined with exactly one free variable
     */
	List<?> closeAll(Object... vars) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		for (Object var : vars) { results.add(closeOne(var)); }
		return results;
	}
	
    /**
     * Invokes this closure once for each passed set of variables.
     * Each iterable is used as a different set of variables with which this closure is invoked
     * @param vars The variables used to invoke this closure once for each set of variables
     * @return A list of Object containing the results of each closure invocation
     * @throws WrongClosureInvocationException if the number of the passed variables doesn't correspond to one
     * with which this closure has been defined
     */
	List<?> closeAll(Iterable<?>... vars) throws WrongClosureInvocationException {
		List<Object> results = new ArrayList<Object>();
		
		int length = vars.length;
		Iterator<?>[] iterators = new Iterator<?>[length];
		for (int i = 0; i < length; i++) { iterators[i] = vars[i].iterator(); }

		while (true) {
			Object[] varSet = new Object[length];
            if (buildParams(length, iterators, varSet)) break;
			results.add(closeOne(varSet));
		}
		
		return results;
	}

    private boolean buildParams(int length, Iterator<?>[] iterators, Object[] varSet) {
        for (int i = 0; i < length; i++) {
            if (!iterators[i].hasNext()) return true;
            varSet[i] = iterators[i].next();
        }
        return false;
    }

    private List<Object[]> bindParams(Object... vars) throws WrongClosureInvocationException {
        if (checkParams(vars)) return null;
		int varCounter = isClosedOnFreeVar() ? 1 : 0;
		int curriedParamCounter = 0;
		List<Object[]> boundParams = new ArrayList<Object[]>();
		for (Object[] args : argsList) {
			if (args == null) {
                boundParams.add(null);
                continue;
            }
            Object[] objs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                switch (getClosureVarType(args[i])) {
                    case VAR:
                        objs[i] = (curriedVars != null && curriedVarsFlags[curriedParamCounter]) ?
                                curriedVars[curriedParamCounter] :
                                getClosureVarArgument(args[i]).evaluate(vars[varCounter++]);
                        curriedParamCounter++;
                        break;
                    case FINAL_VAR:
                        objs[i] = (curriedVars != null && curriedVarsFlags[curriedParamCounter]) ?
                                curriedVars[curriedParamCounter] :
                                vars[varCounter++];
                        curriedParamCounter++;
                        break;
                    case FIXED:
                        objs[i] = args[i];
                        break;

                }
            }
            boundParams.add(objs);
		}
		return boundParams;
	}

    private boolean checkParams(Object... vars) {
        if (vars == null || vars.length == 0) {
            if (freeVarsNumber != 0)
                throw new WrongClosureInvocationException("Closure invoked without vars instead of the expected " + freeVarsNumber);
            if (curriedVars == null && argsList == null) return true;
        }
        if (freeVarsNumber != vars.length)
            throw new WrongClosureInvocationException("Closure invoked with " + vars.length + " vars instead of the expected " + freeVarsNumber);
        if (isClosedOnFreeVar()) checkClosedType(vars[0]);
        return false;
    }

    private void checkClosedType(Object toBeClosed) {
        if (!((Class<?>)closed).isInstance(toBeClosed))
            throw new WrongClosureInvocationException("The first var must be of class " + closed);
    }
	
    /**
     * Curry this closure by fixing one of its free variable to a given value.
     * @param curriedClosure The closure resulting from this curry operation
     * @param curried The value to which the free variable should be curry
     * @param position The 1-based position of the variable to which apply the curry operation
     * @return A Closure having a free variable less than this one since one of them has been fixed to the given value
     * @throws IllegalArgumentException if this closure doesn't have a free variable in the specified position
     */
	<T extends AbstractClosure> T curry(T curriedClosure, Object curried, int position) throws IllegalArgumentException {
        cloneClosureForCurry(curriedClosure);

		curriedClosure.curryParam(curried, position);
		return curriedClosure;
	}

    private <T extends AbstractClosure> void cloneClosureForCurry(T curriedClosure) {
        curriedClosure.closed = closed;
        curriedClosure.invokables = invokables;
        curriedClosure.argsList = argsList;
        curriedClosure.freeVarsNumber = freeVarsNumber;

        if (curriedVars != null) {
            curriedClosure.curriedVars = new Object[curriedVars.length];
            System.arraycopy(curriedVars, 0, curriedClosure.curriedVars, 0, curriedVars.length);
        }
        if (curriedVarsFlags != null) {
            curriedClosure.curriedVarsFlags = new boolean[curriedVarsFlags.length];
            System.arraycopy(curriedVarsFlags, 0, curriedClosure.curriedVarsFlags, 0, curriedVarsFlags.length);
        }
    }

    void curryParam(Object curried, int position) throws IllegalArgumentException {
        if (checkCurriedOnClosed(curried, position)) return;

        if (curriedVars == null) {
			curriedVars = new Object[freeVarsNumber];
			curriedVarsFlags = new boolean[freeVarsNumber];
		}
		
        if (!findVarToBeCurried(curried, position))
		    throw new IllegalArgumentException("Trying to curry this closure on an already bound or unexisting variable");
	}

    private boolean checkCurriedOnClosed(Object curried, int position) {
        if (position != 1 || !isClosedOnFreeVar()) return false;
        checkClosedType(curried);
        closed = curried;
        freeVarsNumber--;
        return true;
    }

    private boolean findVarToBeCurried(Object curried, int position) {
        int counter = position;
        for (int i = 0; i < curriedVars.length; i++) {
            if (curriedVarsFlags[i]) continue;
            if (--counter == 0) {
                curriedVars[i] = curried;
                curriedVarsFlags[i] = true;
                freeVarsNumber--;
                return true;
            }
        }
        return false;
    }
}
