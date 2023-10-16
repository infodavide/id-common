package org.infodavid.commons.service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Property;
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
public interface ApplicationService extends EntityService<Long, Property>, Saveable {

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
    void deleteByName(String name) throws ServiceException, IllegalAccessException;

    /**
     * Find by name.
     * @param name the name
     * @return the collection
     * @throws ServiceException the service exception
     */
    Optional<Property> findByName(String name) throws ServiceException;

    /**
     * Find by scope.
     * @param scope the scope
     * @return the collection
     * @throws ServiceException the service exception
     */
    List<Property> findByScope(String scope) throws ServiceException;

    /**
     * Gets the application build number.
     * @return the build number
     */
    String getBuild();

    /**
     * Gets the information.
     * @return the information
     */
    Map<String, String[]> getInformation();

    /**
     * Gets the application name.
     * @return the name
     */
    String getName();

    /**
     * Gets the references.
     * @param criteria the criteria
     * @return the references
     * @throws ServiceException the service exception
     */
    Collection<EntityReference<Long>> getReferences(Map<String, Object> criteria) throws ServiceException;

    /**
     * Gets the root directory of the application.
     * @return the root directory
     */
    Path getRootDirectory();

    /**
     * Gets the up time in seconds.
     * @return the up time
     */
    long getUpTime();

    /**
     * Gets the application version.
     * @return the version
     */

    String getVersion();

    /**
     * Removes the listener.
     * @param listener the listener
     */
    void removeListener(PropertyChangedListener listener);
}