package org.infodavid.commons.persistence.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;

/**
 * The Interface DefaultDao.
 * @param <K> the key type
 * @param <T> the generic type
 */
public interface DefaultDao<K extends Serializable, T extends PersistentObject<K>> {

    /**
     * Count.
     * @return the long
     */
    long count();

    /**
     * Delete.
     * @param entity the entity
     */
    void delete(T entity);

    /**
     * Delete all.
     */
    void deleteAll();

    /**
     * Delete all.
     * @param entities the entities
     */
    void deleteAll(Iterable<? extends T> entities);

    /**
     * Delete all by identifiers.
     * @param identifiers the identifiers
     */
    void deleteAllById(Iterable<? extends K> identifiers);

    /**
     * Delete by identifier.
     * @param id the identifier
     */
    void deleteById(K id);

    /**
     * Exists by identifier.
     * @param id the identifier
     * @return true, if successful
     */
    boolean existsById(K id);

    /**
     * Find all.
     * @return the iterable
     */
    Iterable<T> findAll();

    /**
     * Find all.
     * @param pagination  the pagination parameters
     * @param restriction the restriction
     * @return the page
     */
    Page<T> findAll(Pagination pagination, Restriction restriction);

    /**
     * Find all by identifier.
     * @param identifiers the identifiers
     * @return the iterable
     */
    Iterable<T> findAllById(Iterable<K> identifiers);

    /**
     * Find by identifier.
     * @param id the identifier
     * @return the optional
     */
    Optional<T> findById(K id);

    /**
     * Insert.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the entity
     */
    <S extends T> S insert(S entity);

    /**
     * Insert all.
     * @param <S>      the generic type
     * @param entities the entities
     * @return the iterable
     */
    <S extends T> List<S> insertAll(Iterable<S> entities);

    /**
     * Update.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the entity
     */
    <S extends T> S update(S entity);

    /**
     * Update all.
     * @param <S>      the generic type
     * @param entities the entities
     * @return the iterable
     */
    <S extends T> List<S> updateAll(Iterable<S> entities);
}
