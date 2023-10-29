package org.infodavid.commons.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.service.exception.ServiceException;

/**
 * The Interface Saveable.
 */
public interface Saveable {

    /**
     * Export the data.
     *
     * @param out the output
     * @param format the format
     * @throws ServiceException the service exception
     */
    CompletableFuture<?> backup(OutputStream out, String format) throws ServiceException;

    /**
     * Restore.
     * @param in the input
     * @throws ServiceException the service exception
     */
    CompletableFuture<?> restore(InputStream in) throws ServiceException;
}
