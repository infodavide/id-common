package org.infodavid.commons.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.model.ObjectLink;
import org.infodavid.commons.model.UserGroup;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class UserGroupService.<br>
 */
public interface UserGroupService extends EntityService<Long, UserGroup> , Saveable{

    /**
     * Find by name.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    CompletableFuture<UserGroup> findByName(String value) throws ServiceException;

    /**
     * Find by property.
     * @param name  the name
     * @param value the value
     * @return the collection
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<UserGroup>> findByProperty(String name, Object value) throws ServiceException;

    /**
     * Find by role.
     * @param role the role
     * @return the list
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<UserGroup>> findByRole(String role) throws ServiceException;

    /**
     * Gets the names.
     * @param criteria the criteria
     * @return the names
     * @throws ServiceException the service exception
     */
    CompletableFuture<Collection<ObjectLink>> getNames(Pagination pagination, Restriction restriction) throws ServiceException;

    /**
     * Gets the supported roles.
     * @return the supported roles
     */
    String[] getSupportedRoles();
}
