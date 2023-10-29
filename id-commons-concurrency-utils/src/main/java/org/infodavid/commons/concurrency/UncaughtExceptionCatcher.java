package org.infodavid.commons.concurrency;

import org.slf4j.Logger;

/**
 * The Class UncaughtExceptionLogger.
 * @param <E> the element type
 */
public class UncaughtExceptionCatcher<E extends Exception> extends UncaughtExceptionLogger {

    /** The exception class. */
    private final Class<E> exceptionClass;

    /** The thread. */
    private Thread thread;

    /** The throwable. */
    private Throwable throwable;

    /**
     * Instantiates a new uncaught exception catcher.
     * @param logger         the logger
     * @param exceptionClass the exception class
     */
    public UncaughtExceptionCatcher(final Logger logger, final Class<E> exceptionClass) {
        super(logger);
        this.exceptionClass = exceptionClass;
    }

    /**
     * Check if an error occurred, raise it if the type is the expected one or log it.
     * @throws E the e
     */
    @SuppressWarnings("unchecked")
    public void check() throws E {
        if (throwable == null) {
            return;
        }

        if (exceptionClass.isInstance(throwable)) {
            throw (E) throwable;
        }

        logger.error("An unexpected error occurred", throwable);
    }

    /**
     * Gets the thread.
     * @return the thread
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Gets the throwable.
     * @return the throwable
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.concurrency.UncaughtExceptionLogger#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        thread = t;
        throwable = e;
    }
}
