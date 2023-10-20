package org.infodavid.commons.persistence.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;

import jakarta.persistence.PersistenceException;

/**
 * The Interface DefaultDao.
 * @param <K> the key type
 * @param <T> the generic type
 */
public interface DefaultDao<K extends Serializable, T extends PersistentObject<K>> {

    /**
     * Count.
     * @return the long
     * @throws PersistenceException the persistence exception
     */
    long count() throws PersistenceException;

    /**
     * Delete.
     * @param entity the entity
     * @throws PersistenceException the persistence exception
     */
    void delete(T entity) throws PersistenceException;

    /**
     * Delete all.
     * @throws PersistenceException the persistence exception
     */
    void deleteAll() throws PersistenceException;

    /**
     * Delete all.
     * @param entities the entities
     * @throws PersistenceException the persistence exception
     */
    void deleteAll(Iterable<? extends T> entities) throws PersistenceException;

    /**
     * Delete all by identifiers.
     * @param identifiers the identifiers
     * @throws PersistenceException the persistence exception
     */
    void deleteAllById(Iterable<? extends K> identifiers) throws PersistenceException;

    /**
     * Delete by identifier.
     * @param id the identifier
     * @throws PersistenceException the persistence exception
     */
    void deleteById(K id) throws PersistenceException;

    /**
     * Exists by identifier.
     * @param id the identifier
     * @return true, if successful
     * @throws PersistenceException the persistence exception
     */
    boolean existsById(K id) throws PersistenceException;

    /**
     * Find all.
     * @return the iterable
     * @throws PersistenceException the persistence exception
     */
    Iterable<T> findAll() throws PersistenceException;

    /**
     * Find all.
     * @param pagination  the pagination parameters
     * @param restriction the restriction
     * @return the page
     * @throws PersistenceException the persistence exception
     */
    Page<T> findAll(Pagination pagination, Restriction restriction) throws PersistenceException;

    /**
     * Find all by identifier.
     * @param identifiers the identifiers
     * @return the iterable
     * @throws PersistenceException the persistence exception
     */
    Iterable<T> findAllById(Iterable<K> identifiers) throws PersistenceException;

    /**
     * Find by identifier.
     * @param id the identifier
     * @return the optional
     * @throws PersistenceException the persistence exception
     */
    Optional<T> findById(K id) throws PersistenceException;

    /**
     * Insert.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the entity
     * @throws PersistenceException the persistence exception
     */
    <S extends T> S insert(S entity) throws PersistenceException;

    /**
     * Insert all.
     * @param <S>      the generic type
     * @param entities the entities
     * @return the iterable
     * @throws PersistenceException the persistence exception
     */
    <S extends T> List<S> insertAll(Iterable<S> entities) throws PersistenceException;

    /**
     * Update.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the entity
     * @throws PersistenceException the persistence exception
     */
    <S extends T> S update(S entity) throws PersistenceException;

    /**
     * Update all.
     * @param <S>      the generic type
     * @param entities the entities
     * @return the iterable
     * @throws PersistenceException the persistence exception
     */
    <S extends T> List<S> updateAll(Iterable<S> entities) throws PersistenceException;
}
