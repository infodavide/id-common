package org.infodavid.commons.utility;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ClassLoaderUtilities.
 */
@SuppressWarnings("static-method")
public final class ClassLoaderUtilities {

    /** The Constant ADD_URL_METHOD. */
    private static final String ADD_URL_METHOD = "addURL";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtilities.class);

    /** The singleton. */
    private static WeakReference<ClassLoaderUtilities> instance = null;

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized ClassLoaderUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ClassLoaderUtilities());
        }

        return instance.get();
    }

    /**
     * Instantiates a new util.
     */
    private ClassLoaderUtilities() {
    }

    /**
     * Adds the directory.
     * @param file the file
     * @throws InvocationTargetException the invocation target exception
     */
    @SuppressWarnings("resource")
    public void addDirectory(final File file) throws InvocationTargetException {
        if (file == null) {
            LOGGER.warn("Given path is null");
        }
        else if (!file.isDirectory()) {
            LOGGER.warn("Given path is not a directory");
        }
        else {
            try {
                LOGGER.info("Adding directory: {}", file.getName());
                final URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
                final Method addURLMethod = MethodUtils.getMatchingMethod(URLClassLoader.class, ADD_URL_METHOD, URL.class);
                addURLMethod.setAccessible(true); //NOSONAR FIXME Fix it on Java 9 or later
                addURLMethod.invoke(classLoader, file.toURI().toURL());
            }
            catch (final InvocationTargetException var7) {
                throw var7;
            }
            catch (IllegalArgumentException | MalformedURLException | SecurityException | IllegalAccessException var8) {
                throw new InvocationTargetException(var8);
            }
        }
    }

    /**
     * Adds the jars to the class path.
     * @param file the directory
     * @throws InvocationTargetException the invocation target exception
     */
    @SuppressWarnings("resource")
    public void addJars(final File file) throws InvocationTargetException {
        if (file == null) {
            LOGGER.warn("Given file is null");

            return;
        }

        if (file.isFile()) {
            addJar(file);

            return;
        }

        if (!file.isDirectory()) {
            LOGGER.warn("Given file is not a directory");

            return;
        }

        final Set<URL> urls = new HashSet<>(); // NOSONAR URL is used by classloader
        LOGGER.info("Scanning directory: {} to add binaries", file.getAbsolutePath());

        try {
            for (final File child : file.listFiles((parent, name) ->  name.toLowerCase().endsWith(".jar"))) {
                LOGGER.info("Adding binary: {}", child.getName());
                urls.add(child.toURI().toURL());
            }

            if (urls.isEmpty()) {
                LOGGER.warn("Cannot find any binary file in the '{}' directory", file.getName());
            }
            else {
                final URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
                final Method addURLMethod = MethodUtils.getMatchingMethod(URLClassLoader.class, ADD_URL_METHOD, URL.class);
                addURLMethod.setAccessible(true); //NOSONAR FIXME Fix it on Java 9 or later

                for (final URL url : urls) {
                    addURLMethod.invoke(classLoader, url);
                }
            }
        }
        catch (final InvocationTargetException e) {
            throw e;
        }
        catch (IllegalAccessException | IllegalArgumentException | MalformedURLException | SecurityException e) {
            throw new InvocationTargetException(ExceptionUtils.getRootCause(e));
        }
    }

    /**
     * Adds the jar to the classpath.
     * @param file the jar file
     * @throws InvocationTargetException the invocation target exception
     */
    @SuppressWarnings("resource")
    public void addJar(final File file) throws InvocationTargetException {
        if (file == null) {
            LOGGER.warn("Given file is null");

            return;
        }

        if (file.isDirectory()) {
            addJars(file);

            return;
        }

        if (!file.getName().toLowerCase().endsWith(".jar")) {
            LOGGER.warn("Given file is not a jar file");

            return;
        }

        try {
            final URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
            final Method addURLMethod = MethodUtils.getMatchingMethod(URLClassLoader.class, ADD_URL_METHOD, URL.class);
            addURLMethod.setAccessible(true); //NOSONAR FIXME Fix it on Java 9 or later
            addURLMethod.invoke(classLoader, file.toURI().toURL());
        }
        catch (final InvocationTargetException e) {
            throw e;
        }
        catch (IllegalAccessException | IllegalArgumentException | MalformedURLException | SecurityException e) {
            throw new InvocationTargetException(ExceptionUtils.getRootCause(e));
        }
    }

    /**
     * Gets the jar path.
     * @param clazz the clazz
     * @return the jar path
     */
    @SuppressWarnings("rawtypes")
    public File getJarPath(final Class clazz) {
        if (clazz == null) {
            LOGGER.warn("Given class is null");

            return null;
        }

        final URL url = clazz.getResource('/' + clazz.getName().replace('.', '/') + ".class");

        if ("file".equals(url.getProtocol())) {
            LOGGER.info("Adding jar: {} to classpath", url);

            return new File(url.getFile());
        }

        LOGGER.warn("Unable to find the jar containing class: {}", clazz);

        return null;
    }
}
