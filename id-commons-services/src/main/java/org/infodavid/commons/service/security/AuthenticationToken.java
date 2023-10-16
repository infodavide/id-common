package org.infodavid.commons.service.security;

import java.io.Serializable;
import java.security.Principal;

import org.infodavid.commons.model.User;

/**
 * The Interface AuthenticationToken.
 */
public interface AuthenticationToken extends Principal, Serializable {

    /**
     * Gets the token.
     * @return the token
     */
    Object getToken();

    /**
     * Gets the user.
     * @return the user
     */
    User getUser();
}
