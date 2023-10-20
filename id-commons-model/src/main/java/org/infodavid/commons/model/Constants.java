/*
 *
 */
package org.infodavid.commons.model;

/**
 * The Interface Constants.
 */
public final class Constants {

    /** The administrator role. */
    public static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

    /** The Constant ANONYMOUS. */
    public static final String ANONYMOUS = "anonymous";

    /** The anonymous (unauthenticated) role. */
    public static final String ANONYMOUS_ROLE = "ANONYMOUS";

    /** The Constant APPLICATION_NAME_PROPERTY. */
    public static final String APPLICATION_NAME_PROPERTY = "application.name";

    /** The Constant APPLICATION_PRODUCTION_ENVIRONMENT_PROPERTY. */
    public static final String APPLICATION_PRODUCTION_ENVIRONMENT_PROPERTY = "application.production";

    /** The Constant APPLICATION_VERSION_PROPERTY. */
    public static final String APPLICATION_VERSION_PROPERTY = "application.version";

    /** The Constant BUILD_NUMBER_PROPERTY. */
    public static final String BUILD_NUMBER_PROPERTY = "application.build";

    /** The Constant CONNECTION_STRING_MAX_LENGTH. */
    public static final short CONNECTION_STRING_MAX_LENGTH = 255;

    /** The Constant CUSTOMER_NAME_MAX_LENGTH. */
    public static final byte CUSTOMER_NAME_MAX_LENGTH = 64;

    /** The Constant CUSTOMER_NAME_MIN_LENGTH. */
    public static final byte CUSTOMER_NAME_MIN_LENGTH = 2;

    /** The Constant DATABASE_NAME_MAX_LENGTH. */
    public static final byte DATABASE_NAME_MAX_LENGTH = 48;

    /** The Constant DEFAULT_ADMINISTRATOR. */
    public static final String DEFAULT_ADMINISTRATOR = "admin";

    /** The Constant DEFAULT_PASSWORD. */
    public static final String DEFAULT_PASSWORD = "admin"; // NOSONAR Default one

    /** The Constant DEFAULT_SESSION_INACTIVITY_TIMEOUT. */
    public static final short DEFAULT_SESSION_INACTIVITY_TIMEOUT = 10;

    /** The Constant DRIVER_CLASS_NAME_MAX_LENGTH. */
    public static final short DRIVER_CLASS_NAME_MAX_LENGTH = 255;

    /** The constant EMAIL_MAX_LENGTH. */
    public static final short EMAIL_MAX_LENGTH = 255;

    /** The Constant EXTERNAL_ID_MAX_LENGTH. */
    public static final byte EXTERNAL_ID_MAX_LENGTH = 64;

    /** The Constant EXTERNAL_ID_MIN_LENGTH. */
    public static final byte EXTERNAL_ID_MIN_LENGTH = 2;

    /** The Constant GRANTED_TO_PROPERTY. */
    public static final String GRANTED_TO_PROPERTY = "application.grantedTo";

    /** The constant GROUP_DISPLAY_NAME_MAX_LENGTH. */
    public static final byte GROUP_DISPLAY_NAME_MAX_LENGTH = 96;

    /** The constant GROUP_NAME_MAX_LENGTH. */
    public static final byte GROUP_NAME_MAX_LENGTH = 48;

    /** The constant GROUP_NAME_MIN_LENGTH. */
    public static final byte GROUP_NAME_MIN_LENGTH = 3;

    /** The Constant GUEST. */
    public static final String GUEST = "guest";

    /** The Constant HOSTNAME_MAX_LENGTH. */
    public static final int HOSTNAME_MAX_LENGTH = 128;

    /** The constant ID_MAX_LENGTH. */
    public static final short ID_MAX_LENGTH = 128;

    /** The constant LAST_IP_MAX_LENGTH. */
    public static final byte LAST_IP_MAX_LENGTH = 48;

    /** The Constant MAC_ADDRESS_LENGTH. */
    public static final byte MAC_ADDRESS_LENGTH = 12;

    /** The Constant MIN_VALIDITY_DURATION. */
    public static final byte MIN_VALIDITY_DURATION = 1;

    /** The operator role. */
    public static final String OPERATOR_ROLE = "OPERATOR";

    /** The constant PASSWORD_MAX_LENGTH. */
    public static final byte PASSWORD_MAX_LENGTH = 32;

    /** The constant PASSWORD_MIN_LENGTH. */
    public static final byte PASSWORD_MIN_LENGTH = 4;

    /** The Constant PATH_MAX_LENGTH. */
    public static final int PATH_MAX_LENGTH = 1024;

    /** The Constant PRODUCT_MAX_LENGTH. */
    public static final byte PRODUCT_MAX_LENGTH = 64;

    /** The Constant PRODUCT_MIN_LENGTH. */
    public static final byte PRODUCT_MIN_LENGTH = 2;

    /** The Constant PROPERTY_LABEL_MAX_LENGTH. */
    public static final short PROPERTY_LABEL_MAX_LENGTH = 128;

    /** The Constant PROPERTY_NAME_MAX_LENGTH. */
    public static final byte PROPERTY_NAME_MAX_LENGTH = 48;

    /** The Constant PROPERTY_TYPE_MAX_LENGTH. */
    public static final byte PROPERTY_TYPE_MAX_LENGTH = 32;

    /** The constant PROPERTY_VALUE_MAX_LENGTH. */
    public static final short PROPERTY_VALUE_MAX_LENGTH = 1024;

    /** The Constant SCHEMA_VERSION_PROPERTY. */
    public static final String SCHEMA_VERSION_PROPERTY = "schema.version";

    /** The Constant SESSION_INACTIVITY_TIMEOUT_PROPERTY. */
    public static final String SESSION_INACTIVITY_TIMEOUT_PROPERTY = "session.inactivityTimeout";

    /** The constant USER_DISPLAY_NAME_MAX_LENGTH. */
    public static final byte USER_DISPLAY_NAME_MAX_LENGTH = 96;

    /** The constant USER_NAME_MAX_LENGTH. */
    public static final byte USER_NAME_MAX_LENGTH = 48;

    /** The constant USER_NAME_MIN_LENGTH. */
    public static final byte USER_NAME_MIN_LENGTH = 3;

    /** The user role. */
    public static final String USER_ROLE = "USER";

    /** The Constant USER_ROLE_MAX_LENGTH. */
    public static final byte USER_ROLE_MAX_LENGTH = 16;

    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    /**
     * Instantiates a new constants.
     */
    private Constants() {
    }
}
