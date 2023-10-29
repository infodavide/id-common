package org.infodavid.commons.utility;

import java.util.concurrent.Callable;

/**
 * The Class ProcessingDuration.
 * @param <V> the value type
 */
public class ProcessingDuration<V> {

    /** The callable. */
    private final Callable<V> callable;

    /**
     * Instantiates a new time counter.
     * @param callable the callable
     */
    public ProcessingDuration(final Callable<V> callable) {
        this.callable = callable;
    }

    /** The duration in nanoseconds. */
    private long duration = 0;

    /**
     * Run.
     * @return the result
     * @throws Exception the exception
     */
    public V run() throws Exception {
        final long t1 = System.nanoTime();
        final V result;

        try {
            result = callable.call();
        }
        finally {
            duration = System.nanoTime() - t1;
        }

        return result;
    }

    /**
     * Gets the duration in nanoseconds.
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }
}
