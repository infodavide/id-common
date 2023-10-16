package org.infodavid.commons.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.infodavid.commons.service.listener.PropertyChangedListener;

/**
 * The Interface MailService.
 */
public interface MailService extends PropertyChangedListener {

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
    boolean send(MimeMessage message, long timeout) throws MessagingException, InterruptedException;
}
