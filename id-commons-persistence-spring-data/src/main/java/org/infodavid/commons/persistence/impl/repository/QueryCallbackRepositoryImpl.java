package org.infodavid.commons.persistence.impl.repository;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.persistence.impl.QueryCallback;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;

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
     * @param entityManager the entity manager
     */
    public QueryCallbackRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.repository.QueryCallbackRepository#find(org.infodavid.commons.persistence.PaginationParameters, org.infodavid.commons.persistence.impl.QueryCallback)
     */
    @Override
    public <E> Page<E> find(final Pagination parameters, final QueryCallback<Page<E>> callback) {
        return callback.call(entityManager);
    }
}
