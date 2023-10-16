package org.infodavid.commons.service;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ServiceLocator.
 */
public abstract class ServiceLocator {

    /** The Constant INSTANCE. */
    private static final ServiceLocator INSTANCE;

    /** The Constant LOGGER. */
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(ServiceLocator.class);
        final ServiceLoader<ServiceLocator> loader = ServiceLoader.load(ServiceLocator.class);
        final Iterator<ServiceLocator> ite = loader.iterator();

        if (ite.hasNext()) {
            INSTANCE = ite.next();
            LOGGER.info("Implementation {} installed", INSTANCE.getClass().getName());
        } else {
            INSTANCE = new ServiceLocator() {
                @Override
                public <T> T getService(final Class<T> clazz) {
                    return null;
                }
            };

            LOGGER.warn("No implementation found");
        }
    }

    /**
     * Gets the single instance.
     * @return the instance
     */
    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    /**
     * Instantiates a new service locator.
     */
    protected ServiceLocator() {
        // noop
    }

    /**
     * Gets the service.
     * @param <T>   the generic type
     * @param clazz the class
     * @return the service
     */
    public abstract <T> T getService(Class<T> clazz);
}
