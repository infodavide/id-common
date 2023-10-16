package org.infodavid.commons.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PersistentObject;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class EntityService.
 * @param <K> the key type
 * @param <T> the generic type
 */
public interface EntityService<K extends Serializable,T extends PersistentObject<K>> extends Validator<T> {

    /**
     * Adds the value.
     * @param value the value
     * @return the added entity
     * @throws ServiceException the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    T add(T value) throws ServiceException, IllegalAccessException;

    /**
     * Gets the count using the given criteria.
     * @param criteria the map of criteria
     * @return the count
     * @throws ServiceException the service exception
     */
    long count(Map<String,Object> criteria) throws ServiceException;

    /**
     * Removes the entities using the given criteria.
     * @param criteria the map of criteria
     * @throws ServiceException the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    void delete(Map<String,Object> criteria) throws ServiceException, IllegalAccessException;

    /**
     * Removes the entity using its identifier.
     * @param id the identifier
     * @throws ServiceException the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    void deleteById(K id) throws ServiceException, IllegalAccessException;

    /**
     * Exists.
     * @param value the value
     * @return the entity or empty
     * @throws ServiceException the service exception
     */
    Optional<T> findByUniqueConstraints(T value) throws ServiceException;

    /**
     * Find using criteria, pagination and sort.<br>
     * @param criteria the map of criteria
     * @param pageNumber the page number (use a positive value or a negative one to get the last page)
     * @param pageSize the page size (use a negative value to not use pagination)
     * @param sortBy the sort by statements (for example: 'id asc' or 'id desc')
     * @return page the page describing page properties and the resulting collection of entities
     * @throws ServiceException the service exception
     */
    Page<T> find(Map<String,Object> criteria, int pageNumber, int pageSize, Collection<String> sortBy) throws ServiceException;

    /**
     * Find one.
     * @param id the identifier
     * @return the entity
     * @throws ServiceException the service exception
     */
    Optional<T> findById(K id) throws ServiceException;

    /**
     * Update.
     * @param values the values
     * @return the list of updated entities
     * @throws ServiceException the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    List<T> update(Collection<T> values) throws ServiceException, IllegalAccessException;

    /**
     * Update.
     * @param value the value
     * @return the updated entity
     * @throws ServiceException the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    T update(T value) throws ServiceException, IllegalAccessException;
}
