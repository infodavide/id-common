package org.infodavid.commons.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.infodavid.commons.utility.Comparators.ClassNameComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DeepCloner.
 */
public class CloneBuilder {

    /**
     * The Interface CustomCloneBuilder.
     */
    public interface CustomCloneBuilder {

        /**
         * Accept.
         * @param source the source
         * @return true, if this builder is able to clone or process the source
         */
        boolean accept(Object source);

        /**
         * Clone.
         * @param <T>    the generic type
         * @param source the source
         * @return the clone or source according to the custom processing
         * @throws InstantiationException the instantiation exception
         * @throws IllegalAccessException the illegal access exception
         */
        <T> T clone(T source) throws InstantiationException, IllegalAccessException;
    }

    /**
     * The Interface ImmutableFilter.
     */
    @SuppressWarnings("rawtypes")
    public interface ImmutableFilter {

        /**
         * Checks if is immutable.
         * @param clazz the class
         * @return true, if is immutable
         */
        boolean isImmutable(Class clazz);
    }

    /**
     * The Class ArrayCloneBuilder.
     */
    private final class ArrayCloneBuilder implements CustomCloneBuilder {

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#accept(java.lang.Object)
         */
        @Override
        public boolean accept(final Object source) {
            return source != null && source.getClass().isArray();
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#clone(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public <T> T clone(final T source) throws InstantiationException, IllegalAccessException {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("Value is an array: {} of: {}", source, source.getClass().getComponentType());
            }

            final int length = Array.getLength(source);
            final T result = (T) Array.newInstance(source.getClass().getComponentType(), length);

            try {
                for (int i = 0; i < length; i++) {
                    Array.set(result, i, doClone(Array.get(source, i)));
                }
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException | InstantiationException e) {
                throw new InstantiationException(e.getMessage());
            }

            return result;
        }

    }

    /**
     * The Class CloneableCloneBuilder.
     */
    private static final class CloneableCloneBuilder implements CustomCloneBuilder {

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#accept(java.lang.Object)
         */
        @Override
        public boolean accept(final Object source) {
            return source instanceof Cloneable;
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#clone(java.lang.Object)
         */
        @SuppressWarnings("unchecked")
        @Override
        public <T> T clone(final T source) throws InstantiationException {
            LOGGER.trace("Value is a Cloneable: {} of class: {}", source, source.getClass());

            try {
                return (T) MethodUtils.invokeExactMethod(source, "clone");
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new InstantiationException(e.getMessage());
            }
        }

    }

    /**
     * The Class CloneReference.
     */
    private static final class CloneReference {

        /** The clone. */
        private final Object clone;

        /** The source. */
        private final Object source;

        /**
         * Instantiates a new clone reference.
         * @param source the source
         * @param clone  the clone
         */
        public CloneReference(final Object source, final Object clone) {
            this.clone = clone;
            this.source = source;
        }

        /**
         * Gets the clone.
         * @return the clone
         */
        public Object getClone() {
            return clone;
        }

        /**
         * Gets the source.
         * @return the source
         */
        public Object getSource() {
            return source;
        }
    }

    /**
     * The Class CollectionCloneBuilder.
     */
    private final class CollectionCloneBuilder implements CustomCloneBuilder {

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#accept(java.lang.Object)
         */
        @Override
        public boolean accept(final Object source) {
            return source instanceof Collection;
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#clone(java.lang.Object)
         */
        @SuppressWarnings({
                "unchecked", "rawtypes"
        })
        @Override
        public <T> T clone(final T source) throws InstantiationException, IllegalAccessException {
            LOGGER.trace("Value is a collection, cloning its elements");
            final T result = instantiate(source);
            final Collection sourceCollection = (Collection) source;
            final Collection resultCollection = (Collection) result;

            for (final Object item : sourceCollection) {
                resultCollection.add(doClone(item));
            }

            return result;
        }

    }

    /**
     * The Class DefaultImmutableFilter.
     */
    @SuppressWarnings("rawtypes")
    private final class DefaultImmutableFilter implements ImmutableFilter {

        /** The classes. */
        private final Class[] classes;

        /**
         * Instantiates a new default immutable filter.
         */
        public DefaultImmutableFilter() {
            classes = new Class[] {
                    Character.class, Class.class, String.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, BigInteger.class, BigDecimal.class, URL.class, URI.class
            };
            Arrays.sort(classes, classComparator);
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.ImmutableFilter#isImmutable(java.lang.Class)
         */
        @Override
        public boolean isImmutable(final Class clazz) {
            return Arrays.binarySearch(classes, clazz, classComparator) >= 0;
        }
    }

    /**
     * The Class MapCloneBuilder.
     */
    private final class MapCloneBuilder implements CustomCloneBuilder {

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#accept(java.lang.Object)
         */
        @Override
        public boolean accept(final Object source) {
            return source instanceof Map;
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#clone(java.lang.Object)
         */
        @SuppressWarnings({
                "unchecked", "rawtypes"
        })
        @Override
        public <T> T clone(final T source) throws InstantiationException, IllegalAccessException {
            LOGGER.trace("Value is a map, cloning its keys and elements");
            final T result = instantiate(source);
            final Map sourceMap = (Map) source;
            final Map resultMap = (Map) result;

            for (final Object item : sourceMap.entrySet()) {
                final Map.Entry entry = (Entry) item;
                resultMap.put(doClone(entry.getKey()), doClone(entry.getValue()));
            }

            return result;
        }
    }

    /**
     * The Class PrimitiveCloneBuilder.
     */
    private static final class PrimitiveCloneBuilder implements CustomCloneBuilder {

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#accept(java.lang.Object)
         */
        @Override
        public boolean accept(final Object source) {
            return source != null && source.getClass().isPrimitive();
        }

        /*
         * (non-javadoc)
         * @see org.infodavid.commons.utility.CloneBuilder.CustomCloneBuilder#clone(java.lang.Object)
         */
        @Override
        public <T> T clone(final T source) {
            LOGGER.trace("Value is a primitive or is immutable: {} of class: {}", source, source.getClass());

            return source;
        }
    }

    /**
     * The Class ToStringStyleImpl.
     */
    private static final class ToStringStyleImpl extends MultilineRecursiveToStringStyle {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -2683332477671389711L;

        /**
         * Instantiates a new to string style impl.
         */
        public ToStringStyleImpl() {
            setArrayContentDetail(true);
            setDefaultFullDetail(true);
        }
    }

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CloneBuilder.class);

    /** The class comparator. */
    private final ClassNameComparator classComparator;

    /** The clones by class. */
    @SuppressWarnings("rawtypes")
    private final Map<Class, Map<Integer, CloneReference>> clonesByClass;

    /** The custom clone builders. */
    private final Set<CustomCloneBuilder> customCloneBuilders;

    /** The fields by class. */
    @SuppressWarnings("rawtypes")
    private final Map<Class, List<Field>> fieldsByClass;

    /** The immutable filters. */
    private final Set<ImmutableFilter> immutableFilters;

    /** The use original on instantiation failure. */
    private boolean useOriginalOnInstantiationFailure = false;

    /** The use serialization on instantiation failure. */
    private boolean useSerializationOnInstantiationFailure = true;

    /**
     * Instantiates a new deep cloner.
     */
    public CloneBuilder() {
        classComparator = new Comparators.ClassNameComparator();
        clonesByClass = new HashMap<>();
        fieldsByClass = new HashMap<>();
        immutableFilters = new LinkedHashSet<>();
        customCloneBuilders = new LinkedHashSet<>();
        immutableFilters.add(new DefaultImmutableFilter());
        customCloneBuilders.add(new PrimitiveCloneBuilder());
        customCloneBuilders.add(new ArrayCloneBuilder());
        customCloneBuilders.add(new CloneableCloneBuilder());
        customCloneBuilders.add(new CollectionCloneBuilder());
        customCloneBuilders.add(new MapCloneBuilder());
    }

    /**
     * Clone.
     * @param <T>   the generic type
     * @param value the value
     * @return the clone
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    public <T> T clone(final T value) throws InstantiationException, IllegalAccessException {
        final long start = System.currentTimeMillis();
        clonesByClass.clear();
        final T result = doClone(value);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Value: {}, cloned in: {}ms", value, String.valueOf(System.currentTimeMillis() - start));
        }

        return result;
    }

    /**
     * Compare.
     * @param <T>   the generic type
     * @param value the value
     * @param clone the clone
     * @return true, if successful
     */
    @SuppressWarnings("rawtypes")
    public <T> boolean compare(final T value, final T clone) {
        final ToStringStyleImpl style = new ToStringStyleImpl();
        final StringBuilder buffer = new StringBuilder(); // NOSONAR Use of StringBuffer defined by parent class of style
        String cloneToString = ToStringBuilder.reflectionToString(clone, style, true);
        String valueToString = ToStringBuilder.reflectionToString(value, style, true);

        for (final Entry<Class, Map<Integer, CloneReference>> clazzEntry : clonesByClass.entrySet()) {
            for (final Entry<Integer, CloneReference> hashCodeEntry : clazzEntry.getValue().entrySet()) {
                buffer.setLength(0);
                buffer.append(clazzEntry.getKey().getSimpleName());
                buffer.append('@');
                buffer.append(Integer.toHexString(hashCodeEntry.getKey().intValue()));
                valueToString = valueToString.replaceAll(buffer.toString(), clazzEntry.getKey().getSimpleName());
                buffer.setLength(0);
                buffer.append(clazzEntry.getKey().getSimpleName());
                buffer.append('@');
                buffer.append(Integer.toHexString(System.identityHashCode(hashCodeEntry.getValue().getClone())));
                cloneToString = cloneToString.replaceAll(buffer.toString(), clazzEntry.getKey().getSimpleName());
            }
        }

        final boolean result = valueToString.equals(cloneToString);

        if (result && LOGGER.isDebugEnabled()) {
            LOGGER.debug("Source:\n{}", valueToString);
            LOGGER.debug("Clone:\n{}", cloneToString);
        }

        return result;
    }

    /**
     * Gets the custom clone builders.
     * @return the custom clone builders
     */
    public Set<CustomCloneBuilder> getCustomCloneBuilders() {
        return customCloneBuilders;
    }

    /**
     * Gets the immutable filters.
     * @return the immutableFilters
     */
    public Set<ImmutableFilter> getImmutableFilters() {
        return immutableFilters;
    }

    /**
     * Checks if is use original on instantiation failure.
     * @return the useOriginalOnInstantiationFailure
     */
    public boolean isUseOriginalOnInstantiationFailure() {
        return useOriginalOnInstantiationFailure;
    }

    /**
     * Checks if is use serialization on instantiation failure.
     * @return the useSerializationOnInstantiationFailure
     */
    public boolean isUseSerializationOnInstantiationFailure() {
        return useSerializationOnInstantiationFailure;
    }

    /**
     * Sets the use original on instantiation failure.
     * @param useOriginalOnInstantiationFailure the useOriginalOnInstantiationFailure to set
     */
    public void setUseOriginalOnInstantiationFailure(final boolean useOriginalOnInstantiationFailure) {
        this.useOriginalOnInstantiationFailure = useOriginalOnInstantiationFailure;
    }

    /**
     * Sets the use serialization on instantiation failure.
     * @param useSerializationOnInstantiationFailure the useSerializationOnInstantiationFailure to set
     */
    public void setUseSerializationOnInstantiationFailure(final boolean useSerializationOnInstantiationFailure) {
        this.useSerializationOnInstantiationFailure = useSerializationOnInstantiationFailure;
    }

    /**
     * Do clone operation recursively.
     * @param <T>    the generic type
     * @param source the value
     * @return the clone
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    private <T> T doClone(final T source) throws InstantiationException, IllegalAccessException {
        if (source == null) {
            return source;
        }

        final Class clazz = source.getClass();

        if (isImmutable(clazz)) {
            LOGGER.trace("Value is immutable: {} of class: {}", source, clazz);

            return source;
        }

        final Map<Integer, CloneReference> clones = clonesByClass.get(clazz);

        if (clones != null) {
            final CloneReference reference = clones.get(Integer.valueOf(System.identityHashCode(source)));

            if (reference != null) {
                LOGGER.trace("Value already cloned: {}, using clone: {}", reference.getSource(), reference.getClone());

                return (T) reference.getClone();
            }
        }

        for (final CustomCloneBuilder filter : customCloneBuilders) {
            if (filter.accept(source)) {
                final Object result = filter.clone(source);
                clonesByClass.computeIfAbsent(clazz, k -> new HashMap<>()).put(Integer.valueOf(System.identityHashCode(source)), new CloneReference(source, result));

                return (T) result;
            }
        }

        final T result = instantiate(source);
        clonesByClass.computeIfAbsent(clazz, k -> new HashMap<>()).put(Integer.valueOf(System.identityHashCode(source)), new CloneReference(source, result));
        cloneFields(source, result);

        return result;
    }

    /**
     * Gets the fields.
     * @param type the class
     * @return the fields
     */
    @SuppressWarnings("rawtypes")
    private List<Field> getFields(final Class type) {
        List<Field> result = fieldsByClass.get(type);

        if (result != null) {
            return result;
        }

        result = FieldUtils.getAllFieldsList(type);
        fieldsByClass.put(type, result);

        return result;
    }

    /**
     * Checks if is immutable.
     * @param type the class
     * @return true, if is immutable
     */
    @SuppressWarnings("rawtypes")
    private boolean isImmutable(final Class type) {
        for (final ImmutableFilter filter : immutableFilters) {
            if (filter.isImmutable(type)) {
                return true;
            }
        }

        if (Collections.class.equals(type.getDeclaringClass())) {
            return true;
        }

        if (!Modifier.isFinal(type.getModifiers())) {
            return false;
        }

        final List<Field> fields = getFields(type);

        for (final Field field : fields) {
            if (!Modifier.isFinal(field.getModifiers()) || !isImmutable(field.getType())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Clone fields of source and set them into the clone.
     * @param source the source
     * @param clone  the clone
     * @throws IllegalAccessException the illegal access exception
     * @throws InstantiationException the instantiation exception
     */
    protected void cloneFields(final Object source, final Object clone) throws IllegalAccessException, InstantiationException {
        LOGGER.trace("Cloning fields");
        final List<Field> fields = getFields(source.getClass());

        for (final Field field : fields) {
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                LOGGER.trace("Field is static and final: {}", field.getName());

                continue;
            }

            LOGGER.trace("Cloning field: {}", field.getName());
            FieldUtils.writeField(field, clone, doClone(FieldUtils.readField(field, source, true)), true);
        }
    }

    /**
     * Instantiate.
     * @param <T>    the generic type
     * @param source the source
     * @return the t
     * @throws InstantiationException the instantiation exception
     */
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    protected <T> T instantiate(final T source) throws InstantiationException {
        LOGGER.trace("Instantiating clone instance of value: {} of class: {}", source, source.getClass());
        LOGGER.trace("Trying to use the default constructor");

        try {
            final Constructor constructor = source.getClass().getConstructor();
            constructor.setAccessible(true); // NOSONAR

            return (T) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
            LOGGER.trace("Cannot use default constructor: {}", e.getMessage());
            InstantiationException exception = null;
            Constructor constructor;

            try {
                constructor = source.getClass().getConstructor(source.getClass());
                LOGGER.trace("Trying to use the constructor by copy");
                constructor.setAccessible(true); // NOSONAR

                return (T) constructor.newInstance(source);
            } catch (final InstantiationException e1) {
                exception = e1;
            } catch (@SuppressWarnings("unused") NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e2) {
                exception = new InstantiationException("No constructor by copy for class: " + source.getClass());
            }

            if (useSerializationOnInstantiationFailure) {
                LOGGER.warn("Instantiation failure, using serialization: {}", exception.getMessage());

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream out = new ObjectOutputStream(baos)) {
                    out.writeObject(source);
                    out.flush();

                    return (T) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
                } catch (final IOException | ClassNotFoundException e1) {
                    throw new InstantiationException(e1.getMessage());
                }
            }
            if (useOriginalOnInstantiationFailure) {
                LOGGER.warn("Instantiation failure, using original: {}", exception.getMessage());

                return source;
            }

            throw exception;
        }
    }
}
