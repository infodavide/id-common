package org.infodavid.commons.persistence.impl.repository;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.persistence.impl.QueryCallback;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * The Interface QueryCallbackRepository.
 * @param <T> the generic type
 * @param <K> the key type
 */
@NoRepositoryBean
public interface QueryCallbackRepository<T, K> extends Repository<T, K> {

    /**
     * Find all.
     * @param <E>      the element type
     * @param parameters the pagination parameters
     * @param callback the callback
     * @return the list
     */
    <E> Page<E> find(Pagination parameters, QueryCallback<Page<E>> callback);
}
