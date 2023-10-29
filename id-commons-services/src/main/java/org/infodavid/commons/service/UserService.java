package org.infodavid.commons.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.model.ObjectLink;
import org.infodavid.commons.model.User;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class UserService.<br>
 * Password is stored as provided by the presentation layer, typically in MD5.<br>
 * If validate method receives an empty password, it tries to replace the value by the one from database.<br>
 * If validate method receives an non empty password, it leaves it untouched and the value is stored as it is (typically in MD5).<br>
 * Normally, password are not provided when listing or getting users, this must be handled by the presentation layer.
 */
public interface UserService extends EntityService<Long, User>, Saveable {

    /** The Constant IP_ADDRESS_PROPERTY. */
    public static final String IP_ADDRESS_PROPERTY = "ip-address";

    /**
     * Find by email.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    CompletableFuture<User> findByEmail(String value) throws ServiceException;

    /**
     * Find by name.
     * @param value the value
     * @return the optional
     * @throws ServiceException the service exception
     */
    CompletableFuture<User> findByName(String value) throws ServiceException;

    /**
     * Find by property.
     * @param name  the name
     * @param value the value
     * @return the collection
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<User>> findByProperty(String name, Object value) throws ServiceException;

    /**
     * Find by role.
     * @param role the role
     * @return the list
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<User>> findByRole(String role) throws ServiceException;

    /**
     * Find by status.
     * @param connected the connected
     * @return the list
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<User>> findByStatus(boolean connected) throws ServiceException;

    /**
     * Generate a new password if user has an email, stores it and sends its by email to the user.
     * @param id the identifier
     * @throws ServiceException the service exception
     */
    CompletableFuture<?> generatePassword(Long id) throws ServiceException;

    /**
     * Gets the names.
     * @param pagination  the pagination
     * @param restriction the restriction
     * @return the names
     * @throws ServiceException the service exception
     */
    CompletableFuture<Collection<ObjectLink>> getNames(Pagination pagination, Restriction restriction) throws ServiceException;

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
    CompletableFuture<Boolean> isConnected(String name);
}
