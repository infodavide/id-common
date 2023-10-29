package org.infodavid.commons.persistence.impl.springdata.repository;

import java.util.List;

import org.infodavid.commons.model.ObjectLink;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.UserGroup;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.persistence.dao.UsersGroupDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceException;

/**
 * The Interface UsersGroupRepository.
 */
@org.springframework.stereotype.Repository
public interface UsersGroupRepository extends UsersGroupDao, QueryCallbackRepository<UserGroup, Long> {

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#countByRole(java.lang.String)
     */
    @Override
    @Query("SELECT count(g) FROM UserGroup g WHERE LOWER(g.role) = LOWER(:role)")
    long countByRole(@Param("role") String value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAll(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    default Page<UserGroup> findAll(final Pagination pagination, final Restriction restriction) {
        return find(em -> RepositoryUtils.getInstance().find(UserGroup.class, UserGroup.class, em, null, pagination, restriction));
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#findByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    @Query("SELECT g FROM UserGroup g JOIN g.properties p WHERE p.name = :name AND p.value = :value")
    List<UserGroup> findByProperty(@Param("name") String name, @Param("value") Object value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#findByRole(java.lang.String)
     */
    @Override
    @Query("SELECT g FROM UserGroup g WHERE LOWER(g.role) = LOWER(:role)")
    List<UserGroup> findByRole(@Param("role") String value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#findLinks(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    default Page<ObjectLink> findLinks(final Pagination pagination, final Restriction restriction) throws PersistenceException {
        return find(em -> RepositoryUtils.getInstance().find(UserGroup.class, ObjectLink.class, em, "UserGroupLinkSqlResultSetMapping", pagination, restriction));
    }

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
    default <S extends UserGroup> S insert(final S entity) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insertAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends UserGroup> List<S> insertAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entities);
    }

    /**
     * Save.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the s
     */
    @Transactional
    <S extends UserGroup> S save(S entity);

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#update(org.infodavid.commons.model.ModelObject)
     */
    @Transactional
    @Override
    default <S extends UserGroup> S update(final S entity) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#updateAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends UserGroup> List<S> updateAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entities);
    }
}
