package org.infodavid.commons.persistence.impl.springdata.repository;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.persistence.impl.springdata.QueryCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * The Interface QueryCallbackRepository.
 * @param <T> the generic type
 * @param <K> the key type
 */
@NoRepositoryBean
public interface QueryCallbackRepository<T, K> extends JpaRepository<T, K> {

    /**
     * Find all.
     * @param <E>      the element type
     * @param callback the callback
     * @return the list
     */
    <E> Page<E> find(QueryCallback<Page<E>> callback);
}
