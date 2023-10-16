package org.infodavid.commons.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.User;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class UserService.<br>
 * Password is stored as provided by the presentation layer, typically in MD5.<br>
 * If validate method receives an empty password, it tries to replace the value by the one from database.<br>
 * If validate method receives an non empty password, it leaves it untouched and the value is stored as it is (typically in MD5).<br>
 * Normally, password are not provided when listing or getting users, this must be handled by the presentation layer.
 */
public interface UserService extends EntityService<Long, User>, Saveable {

    /**
     * Find by email.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<User> findByEmail(String value) throws ServiceException;

    /**
     * Find by name.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<User> findByName(String value) throws ServiceException;

    /**
     * Find by property.
     * @param name  the name
     * @param value the value
     * @return the collection
     * @throws ServiceException the service exception
     */
    List<User> findByProperty(String name, Object value) throws ServiceException;

    /**
     * Find by role.
     * @param role the role
     * @return the list
     * @throws ServiceException the service exception
     */
    List<User> findByRole(String role) throws ServiceException;

    /**
     * Find by status.
     * @param connected the connected
     * @return the list
     * @throws ServiceException the service exception
     */
    List<User> findByStatus(boolean connected) throws ServiceException;

    /**
     * Generate a new password if user has an email, stores it and sends its by email to the user.
     * @param id the identifier
     * @throws ServiceException the service exception
     */
    void generate(Long id) throws ServiceException;

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

    /**
     * Checks if is connected.
     * @param name the name
     * @return true, if user is connected or false if not connected or not found
     */
    boolean isConnected(String name);
}
