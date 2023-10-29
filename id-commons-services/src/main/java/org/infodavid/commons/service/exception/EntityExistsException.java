package org.infodavid.commons.service.exception;

import javax.validation.ValidationException;

import org.infodavid.commons.model.ModelObject;

/**
 * The Class EntityExistsException.
 */
public class EntityExistsException extends ValidationException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8377739488098386511L;

    /** The submitted. */
    private final ModelObject<?> submitted;

    /** The existing. */
    private final ModelObject<?> existing;

    /**
     * Instantiates a new exception.
     * @param message   the message
     * @param submitted the submitted
     * @param existing  the existing
     */
    public EntityExistsException(final String message, final ModelObject<?> submitted, final ModelObject<?> existing) {
        super(message);
        this.submitted = submitted;
        this.existing = existing;
    }

    /**
     * Gets the submitted.
     * @return the submitted
     */
    @SuppressWarnings("rawtypes")
    public ModelObject getSubmitted() {
        return submitted;
    }

    /**
     * Gets the existing.
     * @return the existing
     */
    @SuppressWarnings("rawtypes")
    public ModelObject getExisting() {
        return existing;
    }
}
