package org.infodavid.commons.utility;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Class SleepLock.
 */
public class SleepLock {

    /** The lock. */
    private final Lock lock = new ReentrantLock();

    /** The lock condition. */
    private final Condition lockCondition = lock.newCondition();

    /**
     * Instantiates a new lock.
     */
    public SleepLock() {
        // noop
    }

    /**
     * Sleep. Workaround to avoid high CPU load.
     * @param millis the milliseconds
     * @return true, if successful
     * @throws InterruptedException the interrupted exception
     */
    public boolean await(final long millis) throws InterruptedException {
        return await(millis, TimeUnit.MILLISECONDS);
    }

    /**
     * Sleep. Workaround to avoid high CPU load.
     * @param duration the duration
     * @param unit     the unit
     * @return true, if successful
     * @throws InterruptedException the interrupted exception
     */
    public boolean await(final long duration, final TimeUnit unit) throws InterruptedException {
        if (duration <= 0) {
            return true;
        }

        return lockCondition.await(duration, unit); // NOSONAR
    }

    /**
     * Lock.
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Unlock.
     */
    public void unlock() {
        lock.unlock();
    }
}
