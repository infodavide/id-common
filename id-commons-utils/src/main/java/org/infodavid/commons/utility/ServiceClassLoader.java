package org.infodavid.commons.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceConfigurationError;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ServiceClassLoader.
 * @param <S> the generic type
 */
public class ServiceClassLoader<S> {

    /** The Constant PREFIX. */
    public static final String PREFIX = "/META-INF/services/"; // NOSONAR Not an URL

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceClassLoader.class);

    /**
     * Creates a new service class loader for the given type, using the current thread's {@linkplain java.lang.Thread#getContextClassLoader context class loader}.
     * <p>
     * An invocation of this convenience method of the form <blockquote>
     *
     * <pre>
     * ServiceClassLoader.load(<i>class</i>)
     * </pre>
     *
     * </blockquote> is equivalent to <blockquote>
     *
     * <pre>
     * ServiceClassLoader.load(<i>class</i>, Thread.currentThread().getContextClassLoader())
     * </pre>
     *
     * </blockquote>
     * @param <S>   the class of the service type
     * @param clazz The interface or class
     * @return A new implementation loader
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static <S> ServiceClassLoader<S> load(final Class<S> clazz) throws IOException {
        return ServiceClassLoader.load(clazz, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a new implementation loader for the given type and class loader.
     * @param <S>    the type
     * @param clazz  The interface or class
     * @param loader The class loader to be used to load provider-configuration files and provider classes, or <tt>null</tt> if the system class loader (or, failing that, the bootstrap class loader) is to be used
     * @return A new implementation loader
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static <S> ServiceClassLoader<S> load(final Class<S> clazz, final ClassLoader loader) throws IOException {
        return new ServiceClassLoader<>(clazz, loader);
    }

    /** The type. */
    private final Class<S> abstractType;

    /** The classes. */
    private final Set<Class<? extends S>> classes = new HashSet<>();

    /** The loader. */
    private final ClassLoader loader;

    /**
     * Instantiates a new implementation loader.
     * @param abstractType the class or interface
     * @param loader       the loader
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private ServiceClassLoader(final Class<S> abstractType, final ClassLoader loader) throws IOException {
        this.abstractType = Objects.requireNonNull(abstractType, "Type cannot be null");
        this.loader = loader == null ? ClassLoader.getSystemClassLoader() : loader;
        reload();
    }

    /**
     * Checks if is empty.
     * @return true, if is empty
     */
    public boolean isEmpty() {
        return classes.isEmpty();
    }

    /**
     * Iterator.
     * @return the iterator
     */
    public Iterator<Class<? extends S>> iterator() {
        return classes.iterator();
    }

    /**
     * Size.
     * @return the size
     */
    public int size() {
        return classes.size();
    }

    /**
     * Reload.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private synchronized void reload() throws IOException {
        classes.clear();
        final String fullName = PREFIX + abstractType.getName();
        Enumeration<URL> configs;

        try {
            configs = loader.getResources(fullName);
        } catch (final IOException e) {
            throw new ServiceConfigurationError("No configuration found in: " + fullName, e);
        }

        if (configs.hasMoreElements()) {
            final URL url = configs.nextElement();

            try (InputStream in = url.openStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.indexOf(' ') >= 0 || line.indexOf('\t') >= 0 || line.startsWith("#")) {
                        LOGGER.debug("Skipping line: {}", line);

                        continue;
                    }

                    int cp = line.codePointAt(0);

                    if (!Character.isJavaIdentifierStart(cp)) {
                        throw new ServiceConfigurationError(String.format("Invalid class name for %s in content of URL: %s", abstractType.getSimpleName(), url.toString()));
                    }

                    final int length = line.length();

                    for (int i = Character.charCount(cp); i < length; i += Character.charCount(cp)) {
                        cp = line.codePointAt(i);

                        if (!Character.isJavaIdentifierPart(cp) && cp != '.') {
                            throw new ServiceConfigurationError(String.format("Invalid class name for %s in content of URL: %s", abstractType.getSimpleName(), url.toString()));
                        }
                    }

                    Class clazz;

                    try {
                        clazz = Class.forName(line, false, loader);

                        if (!abstractType.isAssignableFrom(clazz)) {
                            throw new ServiceConfigurationError(String.format("Invalid class inheritance for %s in content of URL: %s", abstractType.getSimpleName(), url.toString()));
                        }

                        classes.add(clazz);
                    } catch (final ClassNotFoundException e) {
                        throw new ServiceConfigurationError(String.format("Invalid class for %s in content of URL: %s", line, url.toString()), e);
                    } catch (final Throwable e) { // NOSONAR Catch all
                        throw new ServiceConfigurationError("Cannot instantiate class: " + line, e);
                    }
                }
            } catch (final IOException e) {
                LOGGER.error("Error reading configuration file", e);
            }
        }

        if (classes.isEmpty()) {
            LOGGER.warn("No implementation found");
        }
    }
}
