package org.infodavid.commons.concurrency;

import java.util.concurrent.Callable;

/**
 * The Class ConstantValueCallable.
 * @param <T> the generic type
 */
public class ConstantValueCallable<T> implements Callable<T> {

    /** The value. */
    private final T value;

    /**
     * Instantiates a new constant value callable.
     * @param value the value
     */
    public ConstantValueCallable(final T value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        this.value = value;
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public T call() {
        return value;
    }
}
