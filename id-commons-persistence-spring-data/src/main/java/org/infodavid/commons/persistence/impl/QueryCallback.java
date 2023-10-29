package org.infodavid.commons.persistence.impl;

import jakarta.persistence.EntityManager;

/**
 * The Interface QueryCallback.
 * @param <E> the generic type
 */
@FunctionalInterface
public interface QueryCallback<E> {

    /**
     * Call.
     * @param entityManager the entity manager
     * @return the result
     */
    E call(EntityManager entityManager);
}
