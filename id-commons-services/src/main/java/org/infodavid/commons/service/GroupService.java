package org.infodavid.commons.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Group;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class GroupService.<br>
 */
public interface GroupService extends EntityService<Long, Group> , Saveable{

    /**
     * Find by name.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<Group> findByName(String value) throws ServiceException;

    /**
     * Find by property.
     * @param name  the name
     * @param value the value
     * @return the collection
     * @throws ServiceException the service exception
     */
    List<Group> findByProperty(String name, Object value) throws ServiceException;

    /**
     * Find by role.
     * @param role the role
     * @return the list
     * @throws ServiceException the service exception
     */
    List<Group> findByRole(String role) throws ServiceException;

    /**
     * Gets the names.
     * @param criteria the criteria
     * @return the names
     * @throws ServiceException the service exception
     */
    Collection<EntityReference<Long>> getReferences(Map<String, Object> criteria) throws ServiceException;

    /**
     * Gets the supported roles.
     * @return the supported roles
     */
    String[] getSupportedRoles();
}
