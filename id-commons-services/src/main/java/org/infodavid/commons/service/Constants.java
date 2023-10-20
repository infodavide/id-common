package org.infodavid.commons.service;

/**
 * The Class Constants.
 */
public final class Constants {

    /** The Constant ALL. */
    public static final String ALL = "*";

    /** The Constant ANTISPAM_ALLOWED_OCCURRENCES_PROPERTY. */
    public static final String ANTISPAM_ALLOWED_OCCURRENCES_PROPERTY = "antispam.allowedOccurences";

    /** The Constant ANTISPAM_EXPIRATION_PROPERTY. */
    public static final String ANTISPAM_EXPIRATION_PROPERTY = "antispam.expiration";

    /** The Constant ARCHIVE_DIRECTORY. */
    public static final String ARCHIVE_DIRECTORY = "archives";

    /** The Constant DEFAULT_ANTISPAM_ALLOWED_OCCURENCES. */
    public static final byte DEFAULT_ANTISPAM_ALLOWED_OCCURENCES = 5;

    /** The Constant DEFAULT_ANTISPAM_EXPIRATION. */
    public static final byte DEFAULT_ANTISPAM_EXPIRATION = 60;

    /** The Constant DEFAULT_HOSTNAME. */
    public static final String DEFAULT_HOSTNAME = "localhost";

    /** The Constant DEFAULT_SMTP_FROM. */
    public static final String DEFAULT_SMTP_FROM = "root@localhost";

    /** The Constant IP_ADDRESS_PROPERTY. */
    public static final String IP_ADDRESS_PROPERTY = "ip-address";

    /** The Constant MAIL_PROPERTY_SCOPE. */
    public static final String MAIL_PROPERTY_SCOPE = "mail";

    /** The Constant SCHEDULER_THREADS_PROPERTY. */
    public static final String SCHEDULER_THREADS_PROPERTY = "scheduler.threrads";

    /** The Constant SMTP_FROM_PROPERTY. */
    public static final String SMTP_FROM_PROPERTY = "smtp.from";

    /** The Constant SMTP_HOSTNAME_PROPERTY. */
    public static final String SMTP_HOSTNAME_PROPERTY = "smtp.hostname";

    /** The Constant SMTP_PASSWORD_PROPERTY. */
    public static final String SMTP_PASSWORD_PROPERTY = "smtp.password"; // NOSONAR No password here

    /** The Constant SMTP_PORT_PROPERTY. */
    public static final String SMTP_PORT_PROPERTY = "smtp.port";

    /** The Constant SMTP_USER_PROPERTY. */
    public static final String SMTP_USER_PROPERTY = "smtp.user";

    /**
     * Instantiates a new constants.
     */
    private Constants() {
        // noop
    }
}
