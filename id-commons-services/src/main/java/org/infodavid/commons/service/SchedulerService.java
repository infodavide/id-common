package org.infodavid.commons.service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.infodavid.commons.service.listener.PropertyChangedListener;

/**
 * The Interface SchedulerService.
 */
public interface SchedulerService extends PropertyChangedListener {

    /**
     * Schedule.
     * @param command the command
     * @param delay the delay
     * @param unit the unit
     * @return the scheduled future
     */
    <T> ScheduledFuture<T>  schedule(Runnable command, long delay, TimeUnit unit);

    /**
     * Schedule at fixed rate.
     * @param command the command
     * @param initialDelay the initial delay
     * @param period the period
     * @param unit the unit
     * @return the scheduled future
     */
    <T> ScheduledFuture<T>  scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

    /**
     * Submit.
     * @param <T> the generic type
     * @param task the task
     * @return the future
     */
    <T> CompletableFuture<T> submit(Callable<T> task);

    /**
     * Submit.
     * @param task the task
     * @return the future
     */
    @SuppressWarnings("rawtypes")
    CompletableFuture submit(Runnable task);
}
