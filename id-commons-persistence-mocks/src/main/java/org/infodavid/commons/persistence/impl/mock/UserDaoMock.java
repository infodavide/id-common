package org.infodavid.commons.persistence.impl.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.model.Property;
import org.infodavid.commons.model.User;
import org.infodavid.commons.persistence.dao.UserDao;

import com.github.javafaker.Faker;

import jakarta.persistence.PersistenceException;

/**
 * The Class UserDaoMock.
 */
public class UserDaoMock extends AbstractDefaultDaoMock<Long, User> implements UserDao {

    /**
     * Instantiates a new mock.
     */
    public UserDaoMock() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#countByRole(java.lang.String)
     */
    @Override
    public long countByRole(final String value) throws PersistenceException {
        return findByRole(value).size();
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByEmail(java.lang.String)
     */
    @Override
    public User findByEmail(final String value) throws PersistenceException {
        for (final Entry<Long, User> entry : map.entrySet()) {
            if (StringUtils.equals(entry.getValue().getEmail(), value)) {
                return clone(entry.getValue());
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByProperty(java.lang.String, java.lang.Object)
     */
    @Override
    public List<User> findByProperty(final String name, final Object value) throws PersistenceException {
        final List<User> results = new ArrayList<>();

        for (final User entity : map.values()) {
            final Property p = entity.getProperties().get(name);

            if (p != null && Objects.equals(value, p.getValue())) {
                results.add(entity);
            }
        }

        return clone(results);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.persistence.dao.UserDao#findByRole(java.lang.String)
     */
    @Override
    public List<User> findByRole(final String value) throws PersistenceException {
        final List<User> results = new ArrayList<>();

        for (final User entity : map.values()) {
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
    protected Class<User> getDomainClass() {
        return User.class;
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
    protected User populateEntity(final Faker faker, final User entity) {
        super.populateEntity(faker, entity);
        entity.setConnectionsCount(faker.number().randomNumber());
        entity.setDisplayName(faker.starTrek().character());
        entity.setEmail(faker.internet().emailAddress());
        entity.setExpirationDate(null);
        entity.setIp(faker.internet().ipV4Address());
        entity.setLastConnectionDate(faker.date().past(1, TimeUnit.MINUTES));
        entity.setPassword(faker.crypto().md5());
        entity.setRole("USER");

        return entity;
    }
}
