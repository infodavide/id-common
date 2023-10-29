package org.infodavid.commons.concurrency;

import java.util.concurrent.Callable;

/**
 * The Class NullValueCallable.
 * @param <T> the generic type
 */
public class NullValueCallable<T> implements Callable<T> {

    /** The Constant INSTANCE. */
    @SuppressWarnings("rawtypes")
    private static final NullValueCallable INSTANCE = new NullValueCallable<>();

    /**
     * Instance.
     * @param <T> the generic type
     * @return the callable
     */
    @SuppressWarnings("unchecked")
    public static final <T> Callable<T> instance() {
        return INSTANCE;
    }

    /**
     * Instantiates a new null value callable.
     */
    private NullValueCallable() {
        super();
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public T call() {
        return null;
    }
}
