package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

/**
 * The Class LoggerOutputStream.
 */
public class LoggerOutputStream extends java.io.ByteArrayOutputStream {

    /** The error. */
    private Throwable error = null;

    /** The last logged line. */
    private String lastLoggedLine = null;

    /** The last logging thread. */
    private long lastLoggingThread = 0;

    /** The level. */
    private final Level level;

    /** The line separator. */
    private final String lineSeparator;

    /** The lock. */
    private final Lock lock = new ReentrantLock();

    /** The logger. */
    private final Logger logger;

    /** The logging. */
    private boolean logging = false;

    /** The source. */
    private final String source;

    /**
     * Instantiates a new logger output stream.
     * @param logger the logger
     * @param level  the level
     */
    public LoggerOutputStream(final Logger logger, final Level level) {
        this(logger, level, null);
    }

    /**
     * Instantiates a new logger output stream.
     * @param logger the logger
     * @param level  the level
     * @param source the source
     */
    public LoggerOutputStream(final Logger logger, final Level level, final String source) {
        this.logger = logger;
        this.level = level;
        this.source = source;
        lineSeparator = System.getProperty("line.separator");
    }

    /*
     * (non-Javadoc)
     * @see java.io.ByteArrayOutputStream#close()
     */
    @Override
    public void close() throws java.io.IOException {
        try {
            log(true);
            super.close();
        } catch (final java.io.IOException e) {
            error = e;

            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws java.io.IOException {
        try {
            log(true);
            super.flush();
        } catch (final java.io.IOException e) {
            error = e;

            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * @see java.io.ByteArrayOutputStream#write(byte[], int, int)
     */
    @Override
    public synchronized void write(final byte[] b, final int off, final int len) {
        try {
            super.write(b, off, len);
            log(false);
        } catch (final java.io.IOException e) {
            error = e;

            throw new RuntimeException(ExceptionUtils.getRootCause(e)); // NOSONAR
        }
    }

    /*
     * (non-Javadoc)
     * @see java.io.ByteArrayOutputStream#write(int)
     */
    @Override
    public synchronized void write(final int b) {
        try {
            super.write(b);
            log(false);
        } catch (final java.io.IOException e) {
            error = e;

            throw new RuntimeException(ExceptionUtils.getRootCause(e)); // NOSONAR
        }
    }

    /**
     * Already logged.
     * @param data the data
     * @return true, if successful
     */
    private boolean alreadyLogged(final String data) {
        boolean result = false;

        if (lastLoggedLine != null) {
            final long currentThreadId = Thread.currentThread().getId();

            if (lastLoggedLine.equals(data) && currentThreadId == lastLoggingThread) {
                result = true;
            } else {
                lastLoggedLine = data;
                lastLoggingThread = currentThreadId;
            }
        }

        return result;
    }

    /**
     * Log.
     * @param force the force
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void log(final boolean force) throws java.io.IOException { // NOSONAR
        lock.lock();

        try {
            if (!logging) {
                logging = true;
                final String data = toString(StandardCharsets.UTF_8.name());

                if (data != null && data.trim().length() != 0 && !alreadyLogged(data) && error == null) {
                    final boolean hasLineSeparator = data.endsWith(lineSeparator);

                    if (force || hasLineSeparator) {
                        reset();

                        if (hasLineSeparator) {
                            if (source == null) {
                                for (final String line : data.split(lineSeparator)) {
                                    log(level, line);
                                }
                            } else {
                                for (final String line : data.split(lineSeparator)) {
                                    log(level, source + line);
                                }
                            }
                        } else if (source == null) {
                            log(level, data);
                        } else {
                            log(level, "Std: " + data);
                        }
                    }
                }
                logging = false;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Log.
     * @param level the level
     * @param msg   the message
     */
    private void log(final Level level, final String msg) { // NOSONAR
        if (Level.SEVERE.equals(level)) {
            logger.error(msg);
        } else if (Level.WARNING.equals(level)) {
            logger.warn(msg);
        } else if (Level.INFO.equals(level)) {
            logger.info(msg);
        } else if (Level.CONFIG.equals(level) || Level.FINE.equals(level)) {
            logger.debug(msg);
        } else if (Level.FINER.equals(level) || Level.FINEST.equals(level)) {
            logger.trace(msg);
        } else { // NOSONAR
            logger.info(msg);
        }
    }
}
