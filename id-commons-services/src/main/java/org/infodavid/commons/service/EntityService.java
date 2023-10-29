package org.infodavid.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.ModelObject;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class EntityService.
 * @param <K> the key type
 * @param <T> the generic type
 */
public interface EntityService<K extends Serializable, T extends ModelObject<K>> extends Validator<T> {

    /**
     * Adds the value.
     * @param value the value
     * @return the added entity
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<T> add(T value) throws ServiceException, IllegalAccessException;

    /**
     * Gets the count using the given criteria.
     * @param criteria the map of criteria
     * @return the count
     * @throws ServiceException the service exception
     */
    CompletableFuture<Long> count(Restriction restriction) throws ServiceException;

    /**
     * Removes the entities using the given criteria.
     * @param criteria the map of criteria
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<?> delete(Map<String, Object> criteria) throws ServiceException, IllegalAccessException;

    /**
     * Removes the entity using its identifier.
     * @param id the identifier
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<K> deleteById(K id) throws ServiceException, IllegalAccessException;

    /**
     * Find using criteria, pagination and sort.<br>
     * @param pagination  the pagination parameters
     * @param restriction the restriction
     * @return page the page describing page properties and the resulting collection of entities
     * @throws ServiceException the service exception
     */
    CompletableFuture<Page<T>> find(Pagination pagination, Restriction restriction) throws ServiceException;

    /**
     * Find one.
     * @param id the identifier
     * @return the entity
     * @throws ServiceException the service exception
     */
    CompletableFuture<T> findById(K id) throws ServiceException;

    /**
     * Update.
     * @param values the values
     * @return the list of updated entities
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<List<T>> update(Collection<T> values) throws ServiceException, IllegalAccessException;

    /**
     * Update.
     * @param value the value
     * @return the updated entity
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<T> update(T value) throws ServiceException, IllegalAccessException;
}
