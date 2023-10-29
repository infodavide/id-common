package org.infodavid.commons.concurrency;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.infodavid.commons.utility.SleepLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ThreadUtilities.
 */
@SuppressWarnings("static-method")
public final class ThreadUtilities {

    /** The singleton. */
    private static WeakReference<ThreadUtilities> instance = null;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtilities.class);

    /** The Constant THREAD_INTERRUPTED. */
    public static final String THREAD_INTERRUPTED = "Thread interrupted";

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized ThreadUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new ThreadUtilities());
        }

        return instance.get();
    }

    /**
     * Instantiates a new utilities.
     */
    private ThreadUtilities() {
    }

    /**
     * Delegates the given Callable to {@link CompletableFuture#supplyAsync(Supplier)} and handles checked exceptions accordingly to unchecked exceptions.
     * @param <U>      the function's return type
     * @param callable a function returning the value to be used to complete the returned CompletableFuture
     * @return the new CompletableFuture
     * @see CompletableFuture#supplyAsync(Supplier)
     */
    public <U> CompletableFuture<U> callAsync(final Callable<? extends U> callable) {
        return CompletableFuture.supplyAsync(callable == null ? null : () -> {
            try {
                return callable.call();
            } catch (Error | RuntimeException e) { // NOSONAR Use of Error
                throw e; // Also avoids double wrapping CompletionExceptions below.
            } catch (final Throwable t) { // NOSONAR Use of Throwable
                throw new CompletionException(t);
            }
        });
    }

    /**
     * Delegates the given Callable and Executor to {@link CompletableFuture#supplyAsync(Supplier, Executor)} and handles checked exceptions accordingly to unchecked exceptions.
     * @param <U>      the function's return type
     * @param callable a function returning the value to be used to complete the returned CompletableFuture
     * @param executor the executor to use for asynchronous execution
     * @return the new CompletableFuture
     * @see CompletableFuture#supplyAsync(Supplier, Executor)
     */
    public <U> CompletableFuture<U> callAsync(final Callable<? extends U> callable, final Executor executor) {
        return CompletableFuture.supplyAsync(callable == null ? null : () -> {
            try {
                return callable.call();
            } catch (Error | RuntimeException e) { // NOSONAR Use of Error
                throw e; // Also avoids double wrapping CompletionExceptions below.
            } catch (final Throwable t) { // NOSONAR Use of Throwable
                throw new CompletionException(t);
            }
        }, executor);
    }

    /**
     * Interrupt.
     * @param thread  the thread
     * @param lock    the lock
     * @param timeout the timeout
     * @throws InterruptedException the interrupted exception
     */
    public void interrupt(final Thread thread, final SleepLock lock, final long timeout) throws InterruptedException {
        lock.lock();
        final long endTime = System.currentTimeMillis() + timeout;
        InterruptedException interruption = null;

        try {
            while (thread.isAlive() && System.currentTimeMillis() < endTime) {
                lock.await(50);
            }

            if (thread.isAlive()) {
                thread.interrupt();
            }
        } catch (final InterruptedException e) { // NOSONAR Thread interrupted at the end
            interruption = e;
        } finally {
            lock.unlock();
        }

        if (interruption != null) {
            LOGGER.warn(THREAD_INTERRUPTED, interruption);
            thread.interrupt();
        }
    }

    /**
     * Initialize scheduled executor service.
     * @param caller  the caller
     * @param logger  the logger
     * @param threads the threads count
     * @return the scheduled thread pool executor
     */
    public ScheduledThreadPoolExecutor newScheduledExecutorService(final Class<?> caller, final Logger logger, final int threads) {
        logger.debug("Initializing scheduled pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} set to use {} threads", caller.getSimpleName(), String.valueOf(threads)); // NOSONAR Always written
        final ScheduledThreadPoolExecutor result = new ScheduledThreadPoolExecutor(corePoolSize, newThreadFactory(caller.getSimpleName(), logger), new CallerRunsThreadPolicy(logger));
        result.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        result.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        result.setKeepAliveTime(15, TimeUnit.SECONDS);

        return result;
    }

    /**
     * Initialize scheduled executor service.
     * @param caller  the caller
     * @param logger  the logger
     * @param threads the threads count
     * @return the scheduled thread pool executor
     */
    public ScheduledThreadPoolExecutor newScheduledExecutorService(final String caller, final Logger logger, final int threads) {
        logger.debug("Initializing scheduled pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} set to use {} threads", caller, String.valueOf(threads)); // NOSONAR Always written
        final ScheduledThreadPoolExecutor result = new ScheduledThreadPoolExecutor(corePoolSize, newThreadFactory(caller, logger), new CallerRunsThreadPolicy(logger));
        result.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        result.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        result.setKeepAliveTime(15, TimeUnit.SECONDS);

        return result;
    }

    /**
     * New thread factory.
     * @param caller the caller
     * @param logger the logger
     * @return the thread factory
     */
    public ThreadFactory newThreadFactory(final String caller, final Logger logger) {
        return new ThreadFactoryImpl(caller, new UncaughtExceptionLogger(logger));
    }

    /**
     * New thread pool executor.
     * @param caller    the caller
     * @param logger    the logger
     * @param threads   the threads count
     * @param queueSize the queue size
     * @return the thread pool executor
     */
    public ThreadPoolExecutor newThreadPoolExecutor(final Class<?> caller, final Logger logger, final int threads, final int queueSize) {
        logger.debug("Initializing pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} use {} threads (queue size: {})", caller.getSimpleName(), String.valueOf(threads), String.valueOf(queueSize)); // NOSONAR Always written

        return new ThreadPoolExecutor(corePoolSize, corePoolSize + 128, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), newThreadFactory(caller.getSimpleName(), logger), new CallerRunsThreadPolicy(logger));
    }

    /**
     * New thread pool executor.
     * @param caller    the caller
     * @param logger    the logger
     * @param threads   the threads count
     * @param queueSize the queue size
     * @return the thread pool executor
     */
    public ThreadPoolExecutor newThreadPoolExecutor(final String caller, final Logger logger, final int threads, final int queueSize) {
        logger.debug("Initializing pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} use {} threads (queue size: {})", caller, String.valueOf(threads), String.valueOf(queueSize)); // NOSONAR Always written

        return new ThreadPoolExecutor(corePoolSize, corePoolSize + 128, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), newThreadFactory(caller, logger), new CallerRunsThreadPolicy(logger));
    }

    /**
     * New thread pool executor with discard of old tasks when the queue is full.
     * @param caller    the caller
     * @param logger    the logger
     * @param threads   the threads count
     * @param queueSize the queue size
     * @return the thread pool executor
     */
    public ThreadPoolExecutor newThreadPoolExecutorWithDiscard(final Class<?> caller, final Logger logger, final int threads, final int queueSize) {
        logger.debug("Initializing pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} use {} threads (queue size: {})", caller.getSimpleName(), String.valueOf(threads), String.valueOf(queueSize)); // NOSONAR Always written

        return new ThreadPoolExecutor(corePoolSize, corePoolSize + 128, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), newThreadFactory(caller.getSimpleName(), logger), new DiscardOldestThreadPolicy(logger));
    }

    /**
     * New thread pool executor with discard of old tasks when the queue is full.
     * @param caller    the caller
     * @param logger    the logger
     * @param threads   the threads count
     * @param queueSize the queue size
     * @return the thread pool executor
     */
    public ThreadPoolExecutor newThreadPoolExecutorWithDiscard(final String caller, final Logger logger, final int threads, final int queueSize) {
        logger.debug("Initializing pool with {} thread(s)", String.valueOf(threads)); // NOSONAR Always written
        final int corePoolSize;

        if (threads <= 0) {
            logger.info("Given threads count is wrong: {}, using 1.", String.valueOf(threads)); // NOSONAR Always written
            corePoolSize = 1;
        } else {
            corePoolSize = threads;
        }

        logger.debug("{} use {} threads (queue size: {})", caller, String.valueOf(threads), String.valueOf(queueSize)); // NOSONAR Always written

        return new ThreadPoolExecutor(corePoolSize, corePoolSize + 128, 15, TimeUnit.SECONDS, new LinkedBlockingQueue<>(queueSize), newThreadFactory(caller, logger), new DiscardOldestThreadPolicy(logger));
    }

    /**
     * Reset.
     * @param executor the executor
     */
    public void reset(final ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            LOGGER.info("Reset on executor: {}", executor); // NOSONAR Always written

            try {
                if (executor instanceof final ThreadPoolExecutor threadPool) {
                    for (final Runnable item : threadPool.getQueue()) {
                        threadPool.remove(item);
                    }

                    threadPool.purge();
                    LOGGER.info("Executor status: active={}, queue={}", String.valueOf(threadPool.getActiveCount()), String.valueOf(threadPool.getQueue().size())); // NOSONAR Always written
                }
            } catch (final Exception e) {
                LOGGER.warn("Cannot reset executor: {}", executor, e); // NOSONAR Always written
            }
        }
    }

    /**
     * Shutdown.
     * @param executor the executor
     * @throws InterruptedException the interrupted exception
     */
    public void shutdown(final ExecutorService executor) throws InterruptedException {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();

            try {
                if (!executor.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (final InterruptedException e) {
                try {
                    executor.shutdownNow();
                } catch (final Exception e2) {
                    LOGGER.trace("Shutdown error", e2);
                }

                throw e;
            }
        }
    }
}
