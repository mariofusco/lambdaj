package ch.lambdaj.function.closure;

import java.util.*;

/**
 * A closure with four free variables
 * @author Mario Fusco
 */
public class Closure4<A, B, C, D> extends AbstractClosure {

    /**
      * Invokes this closure once by applying the given quadruple of variables to it.
      * @param var1 The first variable used to invoke this closure
      * @param var2 The second variable used to invoke this closure
      * @param var3 The third variable used to invoke this closure
      * @param var4 The fourth variable used to invoke this closure
      * @return The result of the closure invocation
      */
	public Object apply(A var1, B var2, C var3, D var4) {
		return closeOne(var1, var2, var3, var4);
	}

    /**
     * Invokes this closure once for each passed quadruple of variables.
     * @param vars1 The first set of variables used to invoke this closure once for each variable
     * @param vars2 The second set of variables used to invoke this closure once for each variable
     * @param vars3 The third set of variables used to invoke this closure once for each variable
     * @param vars4 The fourth set of variables used to invoke this closure once for each variable
     * @return A list of Object containing the results of each closure invocation
     */
	public List<?> each(Iterable<? extends A> vars1, Iterable<? extends B> vars2, Iterable<? extends C> vars3, Iterable<? extends D> vars4) {
		return closeAll(vars1, vars2, vars3, vars4);
	}

    /**
     * Curry this closure by fixing its first free variable to a given value.
     * @param curry The value to which the first variable should be curry
     * @return A Closure having two free variables
     */
	public Closure3<B, C, D> curry1(A curry) {
		return curry(new Closure3<B, C, D>(), curry, 1);
	}

    /**
     * Curry this closure by fixing its second free variable to a given value.
     * @param curry The value to which the second variable should be curry
     * @return A Closure having two free variables
     */
	public Closure3<A, C, D> curry2(B curry) {
		return curry(new Closure3<A, C, D>(), curry, 2);
	}

    /**
     * Curry this closure by fixing its third free variable to a given value.
     * @param curry The value to which the third variable should be curry
     * @return A Closure having two free variables
     */
	public Closure3<A, B, D> curry3(C curry) {
		return curry(new Closure3<A, B, D>(), curry, 3);
	}

    /**
     * Curry this closure by fixing its fourth free variable to a given value.
     * @param curry The value to which the third variable should be curry
     * @return A Closure having two free variables
     */
	public Closure3<A, B, C> curry4(D curry) {
		return curry(new Closure3<A, B, C>(), curry, 4);
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
    @Override
    public Closure4<A, B, C, D> of(Object closedObject, String methodName, Object ... args) {
        return (Closure4<A, B, C, D>)super.of(closedObject, methodName, args);
    }
}
