package org.infodavid.commons.concurrency;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;

import org.slf4j.Logger;

/**
 * The Class DiscardOldestThreadPolicy.
 */
public class DiscardOldestThreadPolicy extends DiscardOldestPolicy {

    /** The logger. */
    private final Logger logger;

    /**
     * Instantiates a new policy.
     * @param logger the logger
     */
    public DiscardOldestThreadPolicy(final Logger logger) {
        this.logger = logger;
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy#rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
     */
    @Override
    public void rejectedExecution(final Runnable r, final ThreadPoolExecutor e) {
        if (e.isShutdown()) {
            logger.warn("Rejected task, executor service is not active, task cannot be executed");
        }
        else {
            logger.warn("Discarding old tasks to allow execution of the new one");
            super.rejectedExecution(r, e);
        }
    }
}
