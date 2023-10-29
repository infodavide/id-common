package org.infodavid.commons.persistence.impl.springdata.repository;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.persistence.impl.springdata.QueryCallback;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

/**
 * The Class QueryCallbackRepositoryImpl.
 * @param <T> the generic type
 * @param <K> the key type
 */
public class QueryCallbackRepositoryImpl<T, K> extends SimpleJpaRepository<T, K> implements QueryCallbackRepository<T, K> {

    /** The entity manager. */
    private final EntityManager entityManager;

    /**
     * Instantiates a new query callback repository.
     * @param entityInformation the entity information
     * @param entityManager     the entity manager
     */
    public QueryCallbackRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.springdata.springdata.repository.QueryCallbackRepository#find(org.infodavid.commons.persistence.impl.springdata.springdata.QueryCallback)
     */
    @Override
    public <E> Page<E> find(final QueryCallback<Page<E>> callback) {
        try {
            return callback.call(entityManager);
        } catch (final PersistenceException e) {
            throw e;
        } catch (final Exception e) {
            throw new PersistenceException(e);
        }
    }
}
