package org.infodavid.commons.jdk;

import java.io.Serializable;
import java.lang.Thread.State;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class ThreadEntry.
 */
public class ThreadEntry implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4612581725840781815L;

    /** The daemon. */
    private boolean daemon = false;

    /** The interrupted. */
    private boolean interrupted = false;

    /** The name. */
    private final String name;

    /** The stack trace. */
    private String[] stackTrace = new String[0];

    /** The state. */
    private State state = null;

    /**
     * Instantiates a new thread entry.
     * @param name the name
     */
    public ThreadEntry(final String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the stack trace.
     * @return the stackTrace
     */
    public String[] getStackTrace() {
        return stackTrace;
    }

    /**
     * Gets the state.
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * Checks if is daemon.
     * @return the daemon
     */
    public boolean isDaemon() {
        return daemon;
    }

    /**
     * Checks if is interrupted.
     * @return the interrupted
     */
    public boolean isInterrupted() {
        return interrupted;
    }

    /**
     * Sets the daemon.
     * @param daemon the daemon to set
     */
    public void setDaemon(final boolean daemon) {
        this.daemon = daemon;
    }

    /**
     * Sets the interrupted.
     * @param interrupted the interrupted to set
     */
    public void setInterrupted(final boolean interrupted) {
        this.interrupted = interrupted;
    }

    /**
     * Sets the stack trace.
     * @param stackTrace the stackTrace to set
     */
    public void setStackTrace(final String[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    /**
     * Sets the state.
     * @param state the state to set
     */
    public void setState(final State state) {
        this.state = state;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
