package org.infodavid.commons.service.security;

/**
 * The Interface Constants.
 */
public final class Constants {

    /** The Constant HTTP_AUTHORIZATION_EXPIRATION_HEADER. */
    public static final String HTTP_AUTHORIZATION_EXPIRATION_HEADER = "X-Authorization-Expiration";

    /** The Constant HTTP_AUTHORIZATION_HEADER_TOKEN_PREFIX. */
    public static final String HTTP_AUTHORIZATION_HEADER_TOKEN_PREFIX = "Token ";

    /** The Constant HTTP_AUTHORIZATION_RESPONSE_HEADER. */
    public static final String HTTP_AUTHORIZATION_RESPONSE_HEADER = "X-Authorization";

    /** The Constant HTTP_EXPIRED_AUTHORIZATION_HEADER. */
    public static final String HTTP_EXPIRED_AUTHORIZATION_HEADER = "X-Expired-Authorization";

    /** The Constant HTTP_SESSION_INACTIVITY_TIMEOUT_HEADER. */
    public static final String HTTP_SESSION_INACTIVITY_TIMEOUT_HEADER = "X-Session-Inactivity-Timeout";

    /**
     * Instantiates a new constants.
     */
    private Constants() {
        // noop
    }
}
