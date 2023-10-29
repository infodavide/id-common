package org.infodavid.commons.concurrency;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class ThreadFactoryImpl.
 */
public class ThreadFactoryImpl implements ThreadFactory {

    /** The group. */
    private final ThreadGroup group; // NOSONAR Use of thread group

    /** The thread number. */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /** The name prefix. */
    private final String namePrefix;

    /** The exception handler. */
    private final UncaughtExceptionHandler exceptionHandler;

    /**
     * Instantiates a new thread factory.
     * @param name the name
     * @param exceptionHandler the exception handler
     */
    public ThreadFactoryImpl(final String name, final UncaughtExceptionHandler exceptionHandler) {
        group = Thread.currentThread().getThreadGroup();
        namePrefix = name + "-pool-thread-";
        this.exceptionHandler = exceptionHandler;
    }

    /*
     * (non-Javadoc)
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     */
    @Override
    public Thread newThread(final Runnable r) {
        final Thread result = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);

        if (result.isDaemon()) {
            result.setDaemon(false);
        }

        if (result.getPriority() != Thread.NORM_PRIORITY) {
            result.setPriority(Thread.NORM_PRIORITY);
        }

        result.setUncaughtExceptionHandler(exceptionHandler);

        return result;
    }
}
