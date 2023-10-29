package org.infodavid.commons.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;

import org.slf4j.Logger;

/**
 * The Class UncaughtExceptionLogger.
 */
public class UncaughtExceptionLogger implements UncaughtExceptionHandler {

    /** The logger. */
    protected final Logger logger;

    /**
     * Instantiates a new uncaught exception logger.
     * @param logger the logger
     */
    public UncaughtExceptionLogger(final Logger logger) {
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread,
     * java.lang.Throwable)
     */
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        logger.error("An error occured during processing", e);
    }
}
