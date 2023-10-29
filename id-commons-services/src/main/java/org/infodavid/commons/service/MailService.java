package org.infodavid.commons.service;

import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.infodavid.commons.service.listener.PropertyChangedListener;

/**
 * The Interface MailService.
 */
public interface MailService extends PropertyChangedListener {

    /** The Constant DEFAULT_SMTP_FROM. */
    public static final String DEFAULT_SMTP_FROM = "root@localhost";

    /** The Constant MAIL_PROPERTY_SCOPE. */
    public static final String MAIL_PROPERTY_SCOPE = "mail";

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
     * New mime message.
     * @return the mime message
     * @throws MessagingException the messaging exception
     */
    MimeMessage newMimeMessage() throws MessagingException;

    /**
     * Send.
     * @param message the message
     * @param timeout the timeout in milliseconds
     * @return true, if not rejected
     * @throws MessagingException   the messaging exception
     * @throws InterruptedException the interrupted exception
     */
    CompletableFuture<Boolean> send(MimeMessage message, long timeout) throws MessagingException, InterruptedException;
}
