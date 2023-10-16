package org.infodavid.commons.service.security;

import java.io.IOException;

/**
 * The Interface PasswordPolicy.
 */
public interface PasswordPolicy { // NOSONAR No constant class

    /** The Constant PASSWORD_CHARACTERS. */
    String PASSWORD_CHARACTERS = "!/*_-=(){}[]?#%@|ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz"; // NOSONAR Not a plain text password

    /** The Constant PASSWORD_MIN_LENGTH. */
    byte PASSWORD_MIN_LENGTH = 5;

    /** The Constant PASSWORD_MAX_LENGTH. */
    byte PASSWORD_MAX_LENGTH = 32;

    /**
     * Encrypt password.
     * @param value the value
     * @return the string
     * @throws IOException the exception
     */
    String encryptPassword(String value) throws IOException;

    /**
     * Generate password.
     * @return the string
     * @throws IOException the exception
     */
    String generatePassword() throws IOException;

    /**
     * Validate.
     * @param userId the user identifier
     * @param password the password
     * @throws IOException the exception
     */
    void validate(long userId, String password) throws IOException;
}
