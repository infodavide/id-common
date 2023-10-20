package org.infodavid.commons.persistence.dao;

import java.util.List;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.User;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;

import jakarta.persistence.PersistenceException;

/**
 * The Interface UserDao.
 */
public interface UserDao extends DefaultDao<Long, User> {

    /**
     * Count by role.
     * @param value the role
     * @return the number of rows
     * @throws PersistenceException the persistence exception
     */
    long countByRole(String value) throws PersistenceException;

    /**
     * Selects by email.
     * @param value the email
     * @return the user
     * @throws PersistenceException the persistence exception
     */
    User findByEmail(String value) throws PersistenceException;

    /**
     * Selects by name.
     * @param value the name
     * @return the user
     * @throws PersistenceException the persistence exception
     */
    User findByName(String value) throws PersistenceException;

    /**
     * Selects by the property.
     * @param name  the name of the property
     * @param value the value of property
     * @return the users
     * @throws PersistenceException the persistence exception
     */
    List<User> findByProperty(String name, Object value) throws PersistenceException;

    /**
     * Selects by role.
     * @param value the role
     * @return the users
     * @throws PersistenceException the persistence exception
     */
    List<User> findByRole(String value) throws PersistenceException;

    /**
     * Find references.
     * @param pagination  the pagination parameters
     * @param restriction the restriction
     * @return the collection of references
     * @throws PersistenceException the persistence exception
     */
    Page<EntityReference> findReferences(Pagination pagination, Restriction restriction) throws PersistenceException;
}
