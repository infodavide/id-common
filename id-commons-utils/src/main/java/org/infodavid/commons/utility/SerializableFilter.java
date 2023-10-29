package org.infodavid.commons.utility;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SerializableFilter.
 */
public class SerializableFilter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializableFilter.class);

    /**
     * Instantiates a new filter.
     */
    public SerializableFilter() {
        // noop
    }

    /**
     * Filter.
     * @param value the value
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public Serializable filter(final Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof final Serializable s) {
            return s;
        }

        if (value instanceof final List<?> l) {
            return (Serializable) filterCollection(l, new ArrayList<>());
        }

        if (value instanceof Map) {
            return (Serializable) filterMap((Map<String, ?>) value);
        }

        if (value instanceof final Collection<?> c) {
            return (Serializable) filterCollection(c, new LinkedHashSet<>());
        }

        if (value.getClass().isArray()) {
            return filterArray(value);
        }

        LOGGER.warn("Object is not Serializable and will be ignored: {}", value);

        return null;
    }

    /**
     * Filter.
     * @param source the source
     * @param result the mapped
     * @return the collection
     */
    private Collection<Serializable> filterCollection(final Collection<?> source, final Collection<Serializable> result) {
        for (final Object sourceItem : source) {
            final Serializable serializable = filter(sourceItem);

            if (serializable != null) {
                result.add(serializable);
            }
        }

        return result;
    }

    /**
     * Filter.
     * @param source the source
     * @param mapped the mapped
     * @return the map
     */
    private Map<String, Serializable> filterMap(final Map<String, ?> source) {
        final Map<String, Serializable> result = new LinkedHashMap<>();

        for (final Entry<String, ?> sourceEntry : source.entrySet()) {
            final Serializable serializable = filter(sourceEntry.getValue());

            if (serializable != null) {
                result.put(sourceEntry.getKey(), serializable);
            }
        }

        return result;
    }

    /**
     * Filter array.
     * @param value the value
     * @return the object
     */
    private Serializable[] filterArray(final Object value) {
        final List<Serializable> mapped = new ArrayList<>();

        for (final Object sourceItem : Arrays.asList(value)) {
            final Serializable serializable = filter(sourceItem);

            if (serializable != null) {
                mapped.add(serializable);
            }
        }

        final Object result = Array.newInstance(Serializable.class, mapped.size());

        for (int i = 0; i < mapped.size(); i++) {
            Array.set(result, i, mapped.get(i));
        }

        return (Serializable[]) result;
    }
}
