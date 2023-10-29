package org.infodavid.commons.persistence.impl.springdata.repository;

import java.util.List;

import org.infodavid.commons.model.ApplicationSetting;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.persistence.dao.ApplicationSettingDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceException;

/**
 * The Interface ApplicationSettingRepository.
 */
@org.springframework.stereotype.Repository
public interface ApplicationSettingRepository extends ApplicationSettingDao, QueryCallbackRepository<ApplicationSetting, Long> {

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.ApplicationSettingDao#deleteByName(java.lang.String)
     */
    @Override
    @Transactional
    @Modifying
    @Query("DELETE FROM ApplicationSetting s WHERE s.name = :name")
    void deleteByName(@Param("name") String name) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAll(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    default Page<ApplicationSetting> findAll(final Pagination pagination, final Restriction restriction) {
        return find(em -> RepositoryUtils.getInstance().find(ApplicationSetting.class, ApplicationSetting.class, em, null, pagination, restriction));
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.ApplicationSettingDao#findByScope(java.lang.String)
     */
    @Override
    @Query("SELECT s FROM ApplicationSetting s WHERE LOWER(s.scope) = LOWER(:scope)")
    List<ApplicationSetting> findByScope(@Param("scope") String value) throws PersistenceException;

    /**
     * Flushes all pending changes to the database.
     */
    @Transactional
    void flush();

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insert(org.infodavid.commons.model.ModelObject)
     */
    @Transactional
    @Override
    default <S extends ApplicationSetting> S insert(final S entity) {
        return RepositoryUtils.getInstance().save(ApplicationSettingRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insertAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends ApplicationSetting> List<S> insertAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(ApplicationSettingRepository.class, this, entities);
    }

    /**
     * Save.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the s
     */
    @Transactional
    <S extends ApplicationSetting> S save(S entity);

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#update(org.infodavid.commons.model.ModelObject)
     */
    @Transactional
    @Override
    default <S extends ApplicationSetting> S update(final S entity) {
        return RepositoryUtils.getInstance().save(ApplicationSettingRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#updateAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends ApplicationSetting> List<S> updateAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(ApplicationSettingRepository.class, this, entities);
    }
}
