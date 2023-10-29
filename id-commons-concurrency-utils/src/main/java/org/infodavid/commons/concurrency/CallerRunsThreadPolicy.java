package org.infodavid.commons.concurrency;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

import org.slf4j.Logger;

/**
 * The Class CallerRunsThreadPolicy.
 */
public class CallerRunsThreadPolicy extends CallerRunsPolicy {

    /** The logger. */
    private final Logger logger;

    /**
     * Instantiates a new policy.
     * @param logger the logger
     */
    public CallerRunsThreadPolicy(final Logger logger) {
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
        } else {
            logger.warn("Task as been blocked, it will be executed in the main thread");
            super.rejectedExecution(r, e);
        }
    }
}
