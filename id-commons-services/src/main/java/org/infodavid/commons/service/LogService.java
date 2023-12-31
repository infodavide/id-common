package org.infodavid.commons.service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.model.FileDescription;
import org.infodavid.commons.service.exception.ServiceException;
import org.infodavid.commons.service.listener.PropertyChangedListener;
import org.slf4j.event.Level;

/**
 * The Interface LogService.
 */
public interface LogService extends EntityService<String, FileDescription>, PropertyChangedListener {

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

    /**
     * Gets the configuration file.
     * @return the configuration file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Path getConfigurationFile() throws IOException;

    /**
     * Gets the default log directory.
     * @return the directory
     */
    Path getLogDirectory();

    /**
     * Gets the last lines.
     * @param relativePath the relative path
     * @param lines        the lines
     * @return the last lines
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<String>> tail(String relativePath, short lines) throws ServiceException;

    /**
     * Gets the log level.
     * @return the log level
     */
    Level getLogLevel();

    /**
     * Gets the max history.
     * @return the max history
     */
    short getMaxHistory();

    /**
     * Gets the max log size.
     * @return the max log size
     */
    short getMaxLogSize();

    /**
     * Reload the logging configuration.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void reload() throws IOException;

    /**
     * Writes the log files using zip stream.
     * @param criteria the criteria
     * @param out      the out
     * @throws ServiceException the service exception
     */
    void zip(Map<String, Object> criteria, OutputStream out) throws ServiceException;

    /**
     * Writes the log files using zip stream.
     * @param relativePath the relative path
     * @param out          the out
     * @throws ServiceException the service exception
     */
    void zip(String relativePath, OutputStream out) throws ServiceException;
}
