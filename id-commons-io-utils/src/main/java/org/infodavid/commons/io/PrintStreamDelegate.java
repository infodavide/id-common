package org.infodavid.commons.io;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.slf4j.Logger;

/**
 * The Class PrintStreamDelegate.
 */
public class PrintStreamDelegate extends java.io.PrintStream {

    /** The delegate. */
    private final java.io.PrintStream delegate;

    /** The err. */
    private final java.io.PrintStream err;

    /**
     * Instantiates a new prints the stream delegate.
     * @param delegate the delegate
     * @param logger   the logger
     * @param level    the level
     * @param err      the error
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @SuppressWarnings("resource")
    public PrintStreamDelegate(final java.io.PrintStream delegate, final Logger logger, final Level level, final java.io.PrintStream err) throws UnsupportedEncodingException {
        super(new LoggerOutputStream(logger, level), true, StandardCharsets.UTF_8.name());
        this.delegate = delegate;
        this.err = err;
    }

    /**
     * Instantiates a new prints the stream delegate.
     * @param delegate the delegate
     * @param logger   the logger
     * @param level    the level
     * @param source   the source
     * @param err      the error
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    @SuppressWarnings("resource")
    public PrintStreamDelegate(final java.io.PrintStream delegate, final Logger logger, final Level level, final String source, final java.io.PrintStream err) throws UnsupportedEncodingException {
        super(new LoggerOutputStream(logger, level, source), true, StandardCharsets.UTF_8.name());
        this.delegate = delegate;
        this.err = err;
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#checkError()
     */
    @Override
    public boolean checkError() {
        return delegate.checkError();
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#close()
     */
    @Override
    public void close() {
        try {
            super.close();
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        delegate.close();
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#flush()
     */
    @Override
    public void flush() {
        try {
            super.flush();
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        delegate.flush();
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#format(java.util.Locale, java.lang.String, java.lang.Object[])
     */
    @Override
    public java.io.PrintStream format(final java.util.Locale l, final String format, final Object... args) {
        try {
            super.format(l, format, args);
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        return delegate.format(l, format, args);
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#format(java.lang.String, java.lang.Object[])
     */
    @Override
    public java.io.PrintStream format(final String format, final Object... args) {
        try {
            super.format(format, args);
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        return delegate.format(format, args);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return delegate.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[])
     */
    @Override
    public void write(final byte[] b) throws java.io.IOException {
        try {
            super.write(b);
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        delegate.write(b);
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#write(byte[], int, int)
     */
    @Override
    public void write(final byte[] buf, final int off, final int len) {
        try {
            super.write(buf, off, len);
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        delegate.write(buf, off, len);
    }

    /*
     * (non-Javadoc)
     * @see java.io.PrintStream#write(int)
     */
    @Override
    public void write(final int b) {
        try {
            super.write(b);
        } catch (final Throwable e) { // NOSONAR
            e.printStackTrace(err);
        }

        delegate.write(b);
    }
}
