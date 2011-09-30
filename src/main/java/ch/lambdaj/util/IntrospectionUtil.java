// Modified or written by Lambdascale SRL for inclusion with lambdaj.
// Copyright (c) 2009-2010 Mario Fusco.
// Licensed under the Apache License, Version 2.0 (the "License")

package ch.lambdaj.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * This class consists exclusively of static methods that offer some introspection facilities.
 * @author Mario Fusco
 */
public final class IntrospectionUtil {

	private IntrospectionUtil() {}

    /**
     * Returns the bean compliant name of the property accessed by the given method
     * @param invokedMethod The method to be introspected
     * @return The bean compliant name of the property accessed by the given method
     */
	public static String getPropertyName(Method invokedMethod) {
		String methodName = invokedMethod.getName();
		if ((methodName.startsWith("get") || methodName.startsWith("set")) && methodName.length() > 3) methodName = methodName.substring(3);
		else if (methodName.startsWith("is") && methodName.length() > 2) methodName = methodName.substring(2);
		return methodName.substring(0, 1).toLowerCase(Locale.getDefault()) + methodName.substring(1);
	}

    /**
     * Return the value of named property on the given bean
     * @param bean The bean to be introspected
     * @param propertyName The name of the property to be introspected
     * @return The value of named property on the given bean
     */
	public static Object getPropertyValue(Object bean, String propertyName) {
		if (bean == null) return null;
		int dotPos = propertyName.indexOf('.');
		if (dotPos > 0) return getPropertyValue(getPropertyValue(bean, propertyName.substring(0, dotPos)), propertyName.substring(dotPos + 1));

		String accessorName = propertyName.substring(0, 1).toUpperCase(Locale.getDefault()) + propertyName.substring(1);
		try {
			return bean.getClass().getMethod("get" + accessorName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			return getBooleanPropertyValue(bean, propertyName, accessorName);
		}
	}
	
	private static Object getBooleanPropertyValue(Object bean, String propertyName, String accessorName) {
		try {
			return bean.getClass().getMethod("is" + accessorName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			return getPlainPropertyValue(bean, propertyName);
		}
	}
	
	private static Object getPlainPropertyValue(Object bean, String propertyName) {
		try {
			return bean.getClass().getMethod(propertyName).invoke(bean, (Object[]) null);
		} catch (Exception e) {
			throw new IntrospectionException(e);
		}
	}

    /**
     * Finds the consructor of the given class that matches the given arguments
     * @param clazz The class for which a constructor should be found
     * @param args The arguments that have to be assignable to the parameters of the constructor to be found
     * @return The consructor of the given class that matches the given arguments
     */
    public static <T> Constructor<T> findConstructor(Class<T> clazz, Object... args) {
        return findConstructor(clazz, objectsToClasses(args));
    }

    /**
     * Finds the consructor of the given class having arguments of the given types
     * @param clazz The class for which a constructor should be found
     * @param parameterTypes The types of the parameters of the constructor to be found
     * @return The consructor of the given class having arguments of the given types
     */
    public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            Constructor<T> constructor = discoverConstructor(clazz, parameterTypes);
            if (constructor == null) throw new IntrospectionException(e);
            return constructor;
        }
    }

    private static <T> Constructor<T> discoverConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        for (Constructor<?> c : clazz.getConstructors()) {
            if (areCompatible(c.getParameterTypes(), parameterTypes)) return (Constructor<T>)c;
        }
        return null;
    }


    /**
     * Finds a method of the given class with the given name that matches the given arguments
     * @param clazz The class containing the method should be found
     * @param args The arguments that have to be assignable to the parameters of the method to be found
     * @return The method of the given class with the given name that matches the given arguments
     */
    public static Method findMethod(Class<?> clazz, String methodName, Object... args) {
        return findMethod(clazz, methodName, objectsToClasses(args));
    }

    /**
     * Finds a method of the given class with the given name having arguments of the given types
     * @param clazz The class containing the method should be found
     * @param parameterTypes The types of the parameters of the method to be found
     * @return The method of the given class with the given name having arguments of the given types
     */
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(methodName,  parameterTypes);
        } catch (NoSuchMethodException e) {
            Method method = discoverMethod(clazz, methodName, parameterTypes);
            if (method == null) throw new IntrospectionException(e);
            return method;
        }
    }

    private static Method discoverMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(methodName) && areCompatible(m.getParameterTypes(), parameterTypes)) return m;
        }
        return null;
    }

    private static boolean areCompatible(Class<?>[] methodParams, Class<?>[] actualParam) {
        if (methodParams == null || methodParams.length != actualParam.length) return false;
        for (int i = 0; i < methodParams.length; i++) {
            if (!areCompatible(methodParams[i], actualParam[i])) return false;
        }
        return true;
    }

    private static boolean areCompatible(Class<?> methodParam, Class<?> actualParam) {
        return methodParam.isAssignableFrom(actualParam) ||
                (methodParam.isPrimitive() ? areBoxingCompatible(methodParam, actualParam) : areBoxingCompatible(actualParam, methodParam));
    }

    private static boolean areBoxingCompatible(Class<?> primitiveClass, Class<?> boxedClass) {
         return boxedClass.getSimpleName().toLowerCase(Locale.getDefault()).startsWith(primitiveClass.getSimpleName());
    }

    private static Class<?>[] objectsToClasses(Object... args) {
        Class<?>[] parameterTypes = new Class<?>[args == null ? 0 : args.length];
        for (int i = 0; i < parameterTypes.length; i++) { parameterTypes[i] = args[i].getClass(); }
        return parameterTypes;
    }

    /**
     * Returns a clone of the given original Object
     * @param original The Object to be cloned
     * @return A clone of the original object
     * @throws CloneNotSupportedException if the original object is not cloneable
     */
    public static Object clone(Object original) throws CloneNotSupportedException {
        if (!(original instanceof Cloneable)) throw new CloneNotSupportedException();
        try {
            return original.getClass().getMethod("clone").invoke(original);
        } catch (Exception e) {
            throw new CloneNotSupportedException();
        }
    }
}
