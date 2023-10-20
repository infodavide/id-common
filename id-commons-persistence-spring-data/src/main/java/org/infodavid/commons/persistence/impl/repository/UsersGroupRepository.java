package org.infodavid.commons.persistence.impl.repository;

import java.util.List;

import org.infodavid.commons.model.EntityReference;
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

    @Override
    @Query("SELECT count(g) FROM UserGroup g WHERE LOWER(g.role) = LOWER(:role)")
    long countByRole(@Param("role") String value) throws PersistenceException;

    @Override
    default Page<UserGroup> findAll(final Pagination pagination, final Restriction restriction) {
        return find(pagination, em -> RepositoryUtils.getInstance().find(UserGroup.class, UserGroup.class, em, null, pagination, restriction));
    }

    @Override
    @Query("SELECT g FROM UserGroup g JOIN g.properties p WHERE p.name = :name AND p.value = :value")
    List<UserGroup> findByProperty(@Param("name") String name, @Param("value") Object value) throws PersistenceException;

    @Override
    @Query("SELECT g FROM UserGroup g WHERE LOWER(g.role) = LOWER(:role)")
    List<UserGroup> findByRole(@Param("role") String value) throws PersistenceException;

    @Override
    default Page<EntityReference> findReferences(final Pagination pagination, final Restriction restriction) throws PersistenceException {
        return find(pagination, em -> RepositoryUtils.getInstance().find(UserGroup.class, EntityReference.class, em, "UserGroupReferenceSqlResultSetMapping", pagination, restriction));
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Transactional
    void flush();

    @Transactional
    @Override
    default <S extends UserGroup> S insert(final S entity) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entity);
    }

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

    @Transactional
    @Override
    default <S extends UserGroup> S update(final S entity) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entity);
    }

    @Override
    @Transactional
    default <S extends UserGroup> List<S> updateAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(UsersGroupRepository.class, this, entities);
    }
}
