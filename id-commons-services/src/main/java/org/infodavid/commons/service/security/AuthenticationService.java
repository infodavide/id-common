package org.infodavid.commons.service.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.infodavid.commons.model.User;
import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Class AuthenticationService.
 */
public interface AuthenticationService {

    /**
     * Adds the listener.
     * @param listener the listener
     */
    void addListener(AuthenticationListener listener);

    /**
     * Authenticate.
     * @param login the login
     * @param password the password
     * @param properties the properties (like remote IP address, etc.)
     * @return the authentication associated to the user
     * @throws IllegalAccessException the illegal access exception
     * @throws ServiceException the service exception
     * @throws LoginException the login exception
     */
    AuthenticationToken authenticate(String login, String password, Map<String,String> properties) throws IllegalAccessException, ServiceException, LoginException;

    /**
     * Checks for permission based on role.
     * @param role the role
     * @throws IllegalAccessException the illegal access exception
     * @throws ServiceException the service exception
     */
    void checkRole(String role) throws IllegalAccessException, ServiceException;

    /**
     * Gets the currently authenticated users.
     * @return the identifiers
     */
    Collection<User> getAuthenticatedUsers();

    /**
     * Gets the authentication token.
     * @param userId the user identifier
     * @return the authentication
     */
    AuthenticationToken getAuthentication(Long userId);

    /**
     * Gets the authentication.
     * @param token the token
     * @return the authentication
     * @throws IOException Signals that an I/O exception has occurred.
     */
    AuthenticationToken getAuthentication(String token) throws IOException;

    /**
     * Gets the authentication token associated to the user identifier.
     * @param user the user
     * @return the authentication
     */
    AuthenticationToken getAuthentication(User user);

    /**
     * Gets the listeners.
     * @return the listeners
     */
    Set<AuthenticationListener> getListeners();

    /**
     * Gets the password policy.
     * @return the password policy
     */
    PasswordPolicy getPasswordPolicy();

    /**
     * Gets the user associated to the current security context.
     * @return the user
     * @throws ServiceException the service exception
     */
    User getUser() throws ServiceException;

    /**
     * Gets the user.
     * @param authentication the authentication token
     * @return the user
     */
    User getUser(AuthenticationToken authentication);

    /**
     * Gets the user identifier.
     * @param token the token
     * @return the identifier
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Long getUserId(String token) throws IOException;

    /**
     * Checks for permission.
     * @param role the role
     * @return true, if successful
     * @throws ServiceException the service exception
     */
    boolean hasRole(String role) throws ServiceException;

    /**
     * Invalidate and logout the authenticated user.
     * @param authentication the authentication
     * @param properties the properties (like remote IP address, etc.)
     */
    void invalidate(AuthenticationToken authentication, Map<String,String> properties);

    /**
     * Invalidate and logout the authenticated user.
     * @param user the authenticated user
     * @param properties the properties (like remote IP address, etc.)
     */
    void invalidate(User user, Map<String,String> properties);

    /**
     * Invalidate and logout all authenticated users.
     */
    void invalidateAll();

    /**
     * Checks if is authenticated.
     * @param user the user
     * @return true, if is authenticated
     */
    boolean isAuthenticated(User user);

    /**
     * Removes the listener.
     * @param listener the listener
     * @return true, if successful
     */
    boolean removeListener(AuthenticationListener listener);
}
