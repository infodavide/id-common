package org.infodavid.commons.utility;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;

/**
 * The Class ReflectionUtilities.
 */
@SuppressWarnings("static-method")
public final class ReflectionUtilities {

    /** The singleton. */
    private static WeakReference<ReflectionUtilities> instance = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized ReflectionUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ReflectionUtilities());
        }

        return instance.get();
    }

    /** The object map. */
    private final Map<Class<?>, Class<?>> objectMap;

    /** The primitive map. */
    private final Map<Class<?>, Class<?>> primitiveMap;

    /**
     * Instantiates a new utilities.
     */
    private ReflectionUtilities() {
        primitiveMap = new HashMap<>();
        objectMap = new HashMap<>();
        primitiveMap.put(boolean.class, Boolean.class);
        objectMap.put(Boolean.class, boolean.class);
        primitiveMap.put(byte.class, Byte.class);
        objectMap.put(Byte.class, byte.class);
        primitiveMap.put(char.class, Character.class);
        objectMap.put(Character.class, char.class);
        primitiveMap.put(double.class, Double.class);
        objectMap.put(Double.class, double.class);
        primitiveMap.put(float.class, Float.class);
        objectMap.put(Float.class, float.class);
        primitiveMap.put(int.class, Integer.class);
        objectMap.put(Integer.class, int.class);
        primitiveMap.put(long.class, Long.class);
        objectMap.put(Long.class, long.class);
        primitiveMap.put(short.class, Short.class);
        objectMap.put(Short.class, short.class);
    }

    /**
     * Adds the all methods.
     * @param methods      the methods
     * @param concreteOnly the concrete only flag
     * @param result       the result
     */
    private void addAllMethods(final Method[] methods, final boolean concreteOnly, final Set<Method> result) {
        if (methods == null || methods.length == 0) {
            return;
        }

        if (concreteOnly) {
            result.addAll(Arrays.stream(methods).filter(m -> !Modifier.isAbstract(m.getModifiers())).toList());
        } else {
            Collections.addAll(result, methods);
        }
    }

    /**
     * From primitive.
     * @param type the type
     * @return the class
     */
    public Class<?> fromPrimitive(final Class<?> type) {
        return primitiveMap.get(type);
    }

    /**
     * Gets the all methods in hierarchy.
     * @param type the class
     * @return the all methods in hierarchy
     */
    public Method[] getAllMethodsInHierarchy(final Class<?> type) {
        return getAllMethodsInHierarchy(type, false);
    }

    /**
     * Gets the all methods in hierarchy.
     * @param type         the class
     * @param concreteOnly the concrete only flag
     * @return the all methods in hierarchy
     */
    public Method[] getAllMethodsInHierarchy(final Class<?> type, final boolean concreteOnly) {
        final Set<Method> result = new LinkedHashSet<>();

        addAllMethods(type.getDeclaredMethods(), concreteOnly, result);
        addAllMethods(type.getMethods(), concreteOnly, result);

        if (type.getSuperclass() != null) {
            addAllMethods(getAllMethodsInHierarchy(type.getSuperclass(), concreteOnly), concreteOnly, result);
        }

        return result.toArray(new Method[result.size()]);
    }

    /**
     * To class.
     * @param value the value
     * @return the class
     */
    @SuppressWarnings("rawtypes")
    public Class getClassQuietly(final String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value)) {
            return null;
        }

        try {
            return Class.forName(value);
        } catch (@SuppressWarnings("unused") final ClassNotFoundException e) { // NOSONAR Quietly
            return null;
        }
    }

    /**
     * Gets the method by name.
     * @param type          the class
     * @param name          the name
     * @param caseSensitive the case sensitive
     * @param concreteOnly  the concrete only
     * @param parameters    the parameters
     * @return the method by name
     */
    public Method getMethod(final Class<?> type, final String name, final boolean caseSensitive, final boolean concreteOnly, final Class<?>... parameters) {
        for (final Method method : getAllMethodsInHierarchy(type, concreteOnly)) {
            if ((caseSensitive && method.getName().equals(name) || method.getName().equalsIgnoreCase(name)) && Arrays.equals(parameters, method.getParameterTypes())) {
                return method;
            }
        }

        return null;
    }

    /**
     * Gets the method by name.
     * @param type the class
     * @param name the name
     * @return the method by name
     */
    public Method getMethodByName(final Class<?> type, final String name) {
        return getMethodByName(type, name, true, false);
    }

    /**
     * Gets the method by name.
     * @param type          the class
     * @param name          the name
     * @param caseSensitive the case sensitive flag
     * @param concreteOnly  the concrete only flag
     * @return the method by name
     */
    public Method getMethodByName(final Class<?> type, final String name, final boolean caseSensitive, final boolean concreteOnly) {
        for (final Method method : getAllMethodsInHierarchy(type, concreteOnly)) {
            if (caseSensitive && method.getName().equals(name) || method.getName().equalsIgnoreCase(name)) {
                return method;
            }
        }

        return null;
    }

    /**
     * Checks if is assignable.
     * @param from the from
     * @param to   the to
     * @return true, if is assignable
     */
    public boolean isAssignable(final Class<?> from, final Class<?> to) {
        Class<?> left = from;
        Class<?> right = to;

        if (from.isPrimitive()) {
            left = primitiveMap.get(from);
        }

        if (to.isPrimitive()) {
            right = primitiveMap.get(to);
        }

        if (ClassUtils.isAssignable(left, right, true)) {
            return true;
        }

        if (Number.class.isAssignableFrom(right) && Number.class.isAssignableFrom(left)) {
            final org.infodavid.commons.utility.NumberUtilities utils = org.infodavid.commons.utility.NumberUtilities.getInstance();

            return utils.getMaximumValue(left) > utils.getMaximumValue(right);
        }

        return false;
    }

    /**
     * To primitive.
     * @param type the type
     * @return the class
     */
    public Class<?> toPrimitive(final Class<?> type) {
        return objectMap.get(type);
    }
}
