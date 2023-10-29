package org.infodavid.commons.concurrency;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Class BasicFuture.
 * @param <I> the generic type
 * @param <T> the generic type
 */
public class BasicFuture<I, T> implements Future<T> {

    /** The cancelled. */
    private final AtomicBoolean cancelled = new AtomicBoolean(false);

    /** The completed. */
    private final AtomicBoolean completed = new AtomicBoolean(false);

    /** The input data. */
    private final I input;

    /** The result. */
    private T result;

    /**
     * Instantiates a new basic future.
     * @param input the input
     */
    public BasicFuture(final I input) {
        this.input = input;
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        synchronized (this) {
            if (completed.get()) {
                return false;
            }

            completed.set(true);
            cancelled.set(true);
            notifyAll();
        }

        return true;
    }

    /**
     * Complete.
     * @param result the result
     * @return the t
     */
    public T complete(final T result) {
        synchronized (this) {
            this.result = result;
            completed.set(true);
            cancelled.set(true);
            notifyAll();
        }

        return result;
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        while (!completed.get()) {
            wait(); // NOSONAR Synchronized
        }

        return getResult();
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        final long msecs = unit.toMillis(timeout);

        if (completed.get()) {
            return getResult();
        }

        if (msecs <= 0) {
            throw new TimeoutException();
        }

        long time = 0;

        do {
            if (completed.get()) {
                return getResult();
            }

            wait(100); // NOSONAR Synchronized
            time += 100;
        } while (time < timeout);

        if (completed.get()) {
            return getResult();
        }

        throw new TimeoutException();
    }

    /**
     * Gets the input.
     * @return the input
     */
    public I getInput() {
        return input;
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return cancelled.get();
    }

    /*
     * (non-javadoc)
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return completed.get();
    }

    /**
     * Gets the result.
     * @return the result
     */
    private T getResult() {
        if (cancelled.get()) {
            throw new CancellationException();
        }

        return result;
    }
}