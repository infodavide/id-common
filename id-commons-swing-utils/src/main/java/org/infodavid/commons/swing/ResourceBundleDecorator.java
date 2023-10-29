package org.infodavid.commons.swing;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ResourceBundleDecorator.
 */
public class ResourceBundleDecorator extends ResourceBundle {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleDecorator.class);

    /** The Constant MISSING_RESOURCE_CHAR. */
    public static final char MISSING_RESOURCE_CHAR = '?';

    /** The Constant MISSING_RESOURCE_PATTERN. */
    public static final String MISSING_RESOURCE_PATTERN = MISSING_RESOURCE_CHAR + " %s " + MISSING_RESOURCE_CHAR;

    /** The delegates. */
    private final Set<ResourceBundle> delegates = new LinkedHashSet<>();

    /**
     * Instantiates a new resource bundle decorator.
     */
    public ResourceBundleDecorator() {
    }

    /**
     * Instantiates a new resource bundle decorator.
     * @param delegate the delegate
     */
    public ResourceBundleDecorator(final ResourceBundle delegate) {
        addDelegate(delegate);
    }

    /**
     * Adds the delegate.
     * @param object the new delegate
     */
    public void addDelegate(final ResourceBundle object) {
        if (object == null) {
            return;
        }

        delegates.add(object);
    }

    /**
     * Contains key.
     * @param key the key
     * @return true, if successful
     * @see java.util.ResourceBundle#containsKey(java.lang.String)
     */
    @Override
    public boolean containsKey(final String key) {
        for (final ResourceBundle delegate : delegates) {
            if (delegate.containsKey(key)) {
                return true;
            }
        }

        return false;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof final ResourceBundleDecorator other)) {
            return false;
        }

        return delegates.equals(other.delegates);
    }

    /**
     * Gets the base bundle name.
     * @return the base bundle name
     * @see java.util.ResourceBundle#getBaseBundleName()
     */
    @Override
    public String getBaseBundleName() {
        return delegates.isEmpty() ? null : delegates.iterator().next().getBaseBundleName();
    }

    /**
     * Gets the string.
     * @param key the key
     * @return the string
     * @see java.util.ResourceBundle#getString(java.lang.String)
     */
    public final String getCapitalizedString(final String key) {
        return StringUtils.capitalize(getString(key));
    }

    /**
     * Gets the string.
     * @param key the key
     * @param args the args
     * @return the string
     */
    public final String getCapitalizeString(final String key, final String... args) {
        return getCapitalizedString(getString(key)) + StringUtils.join(args, ' ');
    }

    /**
     * Gets the delegates.
     * @return the delegates
     */
    public Collection<ResourceBundle> getDelegates() {
        return delegates;
    }

    /**
     * Gets the string.
     * @param key the key
     * @param args the arguments
     * @return the string
     */
    public final String getFormattedString(final String key, final Object... args) {
        return MessageFormat.format(getString(key), args);
    }

    /**
     * Gets the keys.
     * @return the keys
     * @see java.util.ResourceBundle#getKeys()
     */
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(delegates.stream().filter(Objects::nonNull).flatMap(delegate -> Collections.list(delegate.getKeys()).stream()).toList());
    }

    /**
     * Gets the locale.
     * @return the locale
     * @see java.util.ResourceBundle#getLocale()
     */
    @Override
    public Locale getLocale() {
        return delegates.isEmpty() ? null : delegates.iterator().next().getLocale();
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(delegates);
    }

    /**
     * Key set.
     * @return the sets the
     * @see java.util.ResourceBundle#keySet()
     */
    @Override
    public Set<String> keySet() {
        return delegates.stream().filter(Objects::nonNull).flatMap(delegate -> delegate.keySet().stream()).collect(Collectors.toSet());
    }

    /**
     * Gets the strings by replacing the keys by their associated string in the given array.
     * @param keys the keys
     * @return the strings
     */
    public final String[] replaceUsingCapitalizedStrings(final String[] keys) {
        if (ArrayUtils.isEmpty(keys)) {
            return keys;
        }

        for (int i = 0; i < keys.length; i++) {
            keys[i] = getCapitalizedString(keys[i]);
        }

        return keys;
    }

    /*
     * (non-javadoc)
     * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
     */
    @Override
    protected Object handleGetObject(final String key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key cannot be emtpy");
        }

        try {
            final Optional<Object> firstPropertyValue = delegates.stream().filter(delegate -> delegate != null && delegate.containsKey(key)).map(delegate -> delegate.getObject(key)).findFirst();

            if (firstPropertyValue.isPresent()) {
                return firstPropertyValue.get();
            }

            return String.format(MISSING_RESOURCE_PATTERN, key);
        }
        catch (final MissingResourceException e) {
            LOGGER.warn("Resource not found: {}", e.getMessage());

            return String.format(MISSING_RESOURCE_PATTERN, key);
        }
    }
}
