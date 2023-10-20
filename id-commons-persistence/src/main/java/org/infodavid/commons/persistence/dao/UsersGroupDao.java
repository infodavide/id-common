package org.infodavid.commons.persistence.dao;

import java.util.List;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.UserGroup;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;

import jakarta.persistence.PersistenceException;

/**
 * The Interface UsersGroupDao.
 */
public interface UsersGroupDao extends DefaultDao<Long, UserGroup> {

    /**
     * Count by role.
     * @param value the role
     * @return the number of rows
     * @throws PersistenceException the persistence exception
     */
    long countByRole(String value) throws PersistenceException;

    /**
     * Selects by name.
     * @param value the name
     * @return the group
     * @throws PersistenceException the persistence exception
     */
    UserGroup findByName(String value) throws PersistenceException;

    /**
     * Selects by the property.
     * @param name  the name of the property
     * @param value the value of property
     * @return the groups
     * @throws PersistenceException the persistence exception
     */
    List<UserGroup> findByProperty(String name, Object value) throws PersistenceException;

    /**
     * Selects by role.
     * @param value the role
     * @return the groups
     * @throws PersistenceException the persistence exception
     */
    List<UserGroup> findByRole(String value) throws PersistenceException;

    /**
     * Find references.
     * @param pagination  the pagination parameters
     * @param restriction the restriction
     * @return the collection of references
     * @throws PersistenceException the persistence exception
     */
    Page<EntityReference> findReferences(Pagination pagination, Restriction restriction) throws PersistenceException;
}
