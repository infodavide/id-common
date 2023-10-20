package org.infodavid.commons.persistence.dao;

import java.util.List;

import org.infodavid.commons.model.ApplicationSetting;

import jakarta.persistence.PersistenceException;

/**
 * The Interface ApplicationSettingDao.
 */
public interface ApplicationSettingDao extends DefaultDao<Long, ApplicationSetting> {

    /**
     * Delete by name.
     * @param name the name
     * @throws PersistenceException the persistence exception
     */
    void deleteByName(String name) throws PersistenceException;

    /**
     * Selects by name.
     * @param value the name
     * @return the property or null
     * @throws PersistenceException the persistence exception
     */
    ApplicationSetting findByName(String value) throws PersistenceException;

    /**
     * Selects by scope.
     * @param value the scope
     * @return the properties
     * @throws PersistenceException the persistence exception
     */
    List<ApplicationSetting> findByScope(String value) throws PersistenceException;
}
