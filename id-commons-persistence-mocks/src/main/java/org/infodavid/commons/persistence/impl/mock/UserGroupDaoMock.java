package org.infodavid.commons.persistence.impl.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.infodavid.commons.model.Property;
import org.infodavid.commons.model.UserGroup;
import org.infodavid.commons.persistence.dao.UsersGroupDao;

import com.github.javafaker.Faker;

import jakarta.persistence.PersistenceException;

/**
 * The Class UserGroupDaoMock.
 */
public class UserGroupDaoMock extends AbstractDefaultDaoMock<Long, UserGroup> implements UsersGroupDao {

    /**
     * Instantiates a new mock.
     */
    public UserGroupDaoMock() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#countByRole(java.lang.String)
     */
    @Override
    public long countByRole(final String value) throws PersistenceException {
        return findByRole(value).size();
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#findByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public List<UserGroup> findByProperty(final String name, final Object value) throws PersistenceException {
        final List<UserGroup> results = new ArrayList<>();

        for (final UserGroup entity : map.values()) {
            final Property p = entity.getProperties().get(name);

            if (p != null && Objects.equals(value, p.getValue())) {
                results.add(entity);
            }
        }

        return clone(results);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UsersGroupDao#findByRole(java.lang.String)
     */
    @Override
    public List<UserGroup> findByRole(final String value) throws PersistenceException {
        final List<UserGroup> results = new ArrayList<>();

        for (final UserGroup entity : map.values()) {
            if (value.equals(entity.getRole())) {
                results.add(entity);
            }
        }

        return clone(results);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.mock.AbstractDefaultDaoMock#getDomainClass()
     */
    @Override
    protected Class<UserGroup> getDomainClass() {
        return UserGroup.class;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.mock.AbstractDefaultDaoMock#getIdClass()
     */
    @Override
    protected Class<Long> getIdClass() {
        return Long.class;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.impl.mock.AbstractDefaultDaoMock#populateEntity(com.github.javafaker.Faker, org.infodavid.commons.model.ModelObject)
     */
    @Override
    protected UserGroup populateEntity(final Faker faker, final UserGroup entity) {
        super.populateEntity(faker, entity);
        entity.setDisplayName(faker.starTrek().specie());
        entity.setRole("USER");

        return entity;
    }
}
