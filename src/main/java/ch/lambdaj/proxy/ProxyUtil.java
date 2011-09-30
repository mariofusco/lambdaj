// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.proxy;


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.*;

/**
 * An utility class of static factory methods that provide facilities to create proxies
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
@SuppressWarnings("unchecked")
public final class ProxyUtil {
	
	private ProxyUtil() { }
	
    // ////////////////////////////////////////////////////////////////////////
    // /// Generic Proxy
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Check if the given class is nor final neither a primitive one
     * @param clazz The class to be checked
     * @return True if the class is proxable, false otherwise
     */
    public static boolean isProxable(Class<?> clazz) {
        return !clazz.isPrimitive() && !Modifier.isFinal(clazz.getModifiers()) && !clazz.isAnonymousClass();
    }

    /**
     * Creates a dynamic proxy
     * @param interceptor The interceptor that manages the invocations to the created proxy
     * @param clazz The class to be proxied
     * @param failSafe If true return null if it is not possible to proxy the request class, otherwise throws an UnproxableClassException
     * @param implementedInterface The interfaces that has to be implemented by the new proxy
     * @return The newly created proxy
     */
    public static <T> T createProxy(InvocationInterceptor interceptor, Class<T> clazz, boolean failSafe, Class<?> ... implementedInterface) {
        if (clazz.isInterface()) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, concatClasses(new Class<?>[] { clazz }, implementedInterface));
        final ProxyIterator proxyIterator = (interceptor instanceof ProxyIterator) ? (ProxyIterator)interceptor : null;
        try {
            if (proxyIterator != null) proxyIterator.enabled = false;
            return (T)createEnhancer(interceptor, clazz, implementedInterface).create();
        } catch (IllegalArgumentException iae) {
            if (Proxy.isProxyClass(clazz)) return (T)createNativeJavaProxy(clazz.getClassLoader(), interceptor, concatClasses(implementedInterface, clazz.getInterfaces()));
            if (isProxable(clazz)) return ClassImposterizer.INSTANCE.imposterise(interceptor, clazz, implementedInterface);
            return manageUnproxableClass(clazz, failSafe);
        } finally {
            if (proxyIterator != null) proxyIterator.enabled = true;
        }
    }

    private static <T> T manageUnproxableClass(Class<T> clazz, boolean failSafe) {
        if (failSafe) return null;
        throw new UnproxableClassException(clazz);
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Iterable Proxy
    // ////////////////////////////////////////////////////////////////////////
    
    /**
     * Creates a proxy of the given class that also decorates it with Iterable interface
     * @param interceptor The object that will intercept all the invocations to the returned proxy
     * @param clazz The class to be proxied
     * @return The newly created proxy
     */
	public static <T> T createIterableProxy(InvocationInterceptor interceptor, Class<T> clazz) {
        if (clazz.isPrimitive()) return null;
        return createProxy(interceptor, normalizeProxiedClass(clazz), false, Iterable.class);
	}

    private static <T> Class<T> normalizeProxiedClass(Class<T> clazz) {
        if (clazz == String.class) return (Class<T>)CharSequence.class;
        return clazz;
    }

    // ////////////////////////////////////////////////////////////////////////
    // /// Private
    // ////////////////////////////////////////////////////////////////////////
    
    private static Enhancer createEnhancer(MethodInterceptor interceptor, Class<?> clazz, Class<?> ... interfaces) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(clazz);
        if (interfaces != null && interfaces.length > 0) enhancer.setInterfaces(interfaces);
        return enhancer;
    }

    private static Object createNativeJavaProxy(ClassLoader classLoader, InvocationHandler interceptor, Class<?> ... interfaces) {
        return Proxy.newProxyInstance(classLoader, interfaces, interceptor);
    }
	
	private static Class<?>[] concatClasses(Class<?>[] first, Class<?>[] second) {
        if (first == null || first.length == 0) return second;
        if (second == null || second.length == 0) return first;
        Class<?>[] concatClasses = new Class[first.length + second.length];
        System.arraycopy(first, 0, concatClasses, 0, first.length);
        System.arraycopy(second, 0, concatClasses, first.length, second.length);
        return concatClasses;
	}
}
