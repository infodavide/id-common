package org.infodavid.commons.service;

import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class Validator.
 * @param <T> the generic type
 */
public interface Validator<T extends PersistentObject<?>> {

    /**
     * Validate.
     * @param value the value
     * @throws ServiceException the service exception
     */
    void validate(T value) throws ServiceException;
}
