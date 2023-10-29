package org.infodavid.commons.service;

import org.infodavid.commons.model.ModelObject;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class Validator.
 * @param <T> the generic type
 */
public interface Validator<T extends ModelObject<?>> {

    /**
     * Validate.
     * @param value the value
     * @throws ServiceException the service exception
     */
    void validate(T value) throws ServiceException;
}
