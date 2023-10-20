package org.infodavid.commons.persistence.impl;

import jakarta.persistence.EntityManager;

/**
 * The Class QueryCallbackWithoutResult.
 * @param <T> the generic type
 */
public abstract class QueryCallbackWithoutResult<T> implements QueryCallback<T> {

    /**
     * Call.
     * @param entityManager the entity manager
     * @return the t
     */
    @Override
    public T call(final EntityManager entityManager) {
        callWithoutResult(entityManager);

        return null;
    }

    /**
     * Call without result.
     * @param entityManager the entity manager
     */
    protected abstract void callWithoutResult(EntityManager entityManager);
}
