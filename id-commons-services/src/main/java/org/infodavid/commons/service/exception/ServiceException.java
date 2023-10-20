package org.infodavid.commons.service.exception;

/**
 * The Class ServiceException.
 */
public class ServiceException extends Exception {

    /** serialVersionUID. */
    private static final long serialVersionUID = 4328558488920679548L;

    /**
     * Instantiates a new exception.
     */
    public ServiceException() {
    }

    /**
     * Instantiates a new exception.
     * @param message the message
     */
    public ServiceException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new exception.
     * @param message the message
     * @param cause the cause
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new exception.
     * @param message the message
     * @param cause the cause
     * @param enableSuppression the enable suppression
     * @param writableStackTrace the writable stack trace
     */
    public ServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Instantiates a new exception.
     * @param cause the cause
     */
    public ServiceException(final Throwable cause) {
        super(cause);
    }
}
