package org.infodavid.commons.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.infodavid.commons.model.ApplicationSetting;
import org.infodavid.commons.service.exception.ServiceException;
import org.infodavid.commons.service.listener.PropertyChangedListener;

/**
 * The Class ApplicationService.<br>
 * If type of the property is Password, its value is stored using a two ways encoding (See StringUtils.encode()).<br>
 * If validate method receives an empty password, it tries to replace the value by the one from database.<br>
 * If validate method receives an non empty password, it encodes the value before update into the DB.<br>
 * Properties of type Password expose a plain password to allow other services to use the values without knowledge of encoding.<br>
 * Normally, password are not provided when listing or getting properties, this must be handled by the presentation layer.
 */
public interface ApplicationService extends EntityService<Long, ApplicationSetting>, Saveable {

    /**
     * Adds the listener.
     * @param listener the listener
     */
    void addListener(PropertyChangedListener listener);

    /**
     * Delete by name.
     * @param name the name
     * @throws ServiceException       the service exception
     * @throws IllegalAccessException the illegal access exception
     */
    CompletableFuture<?> deleteByName(String name) throws ServiceException, IllegalAccessException;

    /**
     * Find by name.
     * @param name the name
     * @return the collection
     * @throws ServiceException the service exception
     */
    CompletableFuture<ApplicationSetting> findByName(String name) throws ServiceException;

    /**
     * Find by scope.
     * @param scope the scope
     * @return the collection
     * @throws ServiceException the service exception
     */
    CompletableFuture<List<ApplicationSetting>> findByScope(String scope) throws ServiceException;

    /**
     * Gets the application build number.
     * @return the build number
     */
    String getBuild();

    /**
     * Gets the information.
     * @return the information
     */
    CompletableFuture<Map<String, String[]>> getInformation();

    /**
     * Gets the application name.
     * @return the name
     */
    String getName();

    /**
     * Gets the root directory of the application.
     * @return the root directory
     */
    Path getRootDirectory();

    /**
     * Gets the up time in seconds.
     * @return the up time
     */
    CompletableFuture<Long> getUpTime();

    /**
     * Gets the application version.
     * @return the version
     */

    String getVersion();

    /**
     * Gets the schema version.
     * @return the version
     */

    String getShcemaVersion();

    /**
     * Removes the listener.
     * @param listener the listener
     */
    void removeListener(PropertyChangedListener listener);
}
