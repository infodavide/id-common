package org.infodavid.commons.persistence.impl.repository;

import java.util.List;

import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.User;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.persistence.dao.UserDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceException;

/**
 * The Interface UserRepository.
 */
@org.springframework.stereotype.Repository
public interface UserRepository extends UserDao, QueryCallbackRepository<User, Long> {

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#countByRole(java.lang.String)
     */
    @Override
    @Query("SELECT count(u) FROM User u WHERE LOWER(u.role) = LOWER(:role)")
    long countByRole(@Param("role") String value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#findAll(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    default Page<User> findAll(final Pagination pagination, final Restriction restriction) {
        return find(em -> RepositoryUtils.getInstance().find(User.class, User.class, em, null, pagination, restriction));
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByEmail(java.lang.String)
     */
    @Override
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    @Query("SELECT u FROM User u JOIN u.properties p WHERE p.name = :name AND p.value = :value")
    List<User> findByProperty(@Param("name") String name, @Param("value") Object value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByRole(java.lang.String)
     */
    @Override
    @Query("SELECT u FROM User u WHERE LOWER(u.role) = LOWER(:role)")
    List<User> findByRole(@Param("role") String value) throws PersistenceException;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findReferences(org.infodavid.commons.model.query.Pagination, org.infodavid.commons.model.query.Restriction)
     */
    @Override
    default Page<EntityReference> findReferences(final Pagination pagination, final Restriction restriction) throws PersistenceException {
        return find(em -> RepositoryUtils.getInstance().find(User.class, EntityReference.class, em, "UserReferenceSqlResultSetMapping", pagination, restriction));
    }

    /**
     * Flushes all pending changes to the database.
     */
    @Transactional
    void flush();

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insert(org.infodavid.commons.model.PersistentObject)
     */
    @Transactional
    @Override
    default <S extends User> S insert(final S entity) {
        return RepositoryUtils.getInstance().save(UserRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#insertAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends User> List<S> insertAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(UserRepository.class, this, entities);
    }

    /**
     * Save.
     * @param <S>    the generic type
     * @param entity the entity
     * @return the s
     */
    @Transactional
    <S extends User> S save(S entity);

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#update(org.infodavid.commons.model.PersistentObject)
     */
    @Transactional
    @Override
    default <S extends User> S update(final S entity) {
        return RepositoryUtils.getInstance().save(UserRepository.class, this, entity);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.DefaultDao#updateAll(java.lang.Iterable)
     */
    @Override
    @Transactional
    default <S extends User> List<S> updateAll(final Iterable<S> entities) {
        return RepositoryUtils.getInstance().save(UserRepository.class, this, entities);
    }
}
