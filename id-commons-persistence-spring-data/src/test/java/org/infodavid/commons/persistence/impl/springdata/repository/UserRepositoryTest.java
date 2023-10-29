package org.infodavid.commons.persistence.impl.springdata.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.Constants;
import org.infodavid.commons.model.ObjectLink;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PropertyType;
import org.infodavid.commons.model.User;
import org.infodavid.commons.model.UserProperty;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.model.query.RestrictionOperator;
import org.infodavid.commons.persistence.dao.UserDao;
import org.infodavid.commons.persistence.impl.springdata.AbstractSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

/**
 * The Class UserRepositoryTest.
 */
@Sql({
        "/users.sql"
})
class UserRepositoryTest extends AbstractSpringTest {

    /** The Constant COUNT. */
    private static final byte COUNT = 5;

    /** The data access object. */
    @Autowired
    private UserDao dao;

    /**
     * Test count.
     * @throws Exception the exception
     */
    @Test
    void testCount() throws Exception {
        assertEquals(COUNT, dao.count(), "Wrong result");
    }

    /**
     * Test delete by id.
     * @throws Exception the exception
     */
    @Test
    void testDeleteById() throws Exception {
        dao.deleteById(Long.valueOf(2));

        final Optional<User> optional = dao.findById(Long.valueOf(2));

        assertNotNull(optional, "Null result");
        assertTrue(optional.isEmpty(), "No result");
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    void testFindAll() throws Exception {
        final Iterable<User> results = dao.findAll();

        assertNotNull(results, "Null result");
        assertEquals(COUNT, size(results), "Wrong number of elements");
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingPagination() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<User> results = dao.findAll(params, null);

        assertNotNull(results, "Null results");
        assertNotNull(results.getResults(), "Null results");
        assertEquals(1, results.getNumber(), "Wrong page number");
        assertEquals(COUNT, results.getTotalSize(), "Wrong total size");
        assertEquals(params.getSize(), results.getSize(), "Wrong size");
        assertEquals(params.getSize(), results.getResults().size(), "Wrong number of results");

        for (final User result : results.getResults()) {
            assertNotNull(result.getProperties(), "Null properties");
            assertNotNull(result.getGroups(), "Null groups");

            if ("anonymous".equals(result.getName())) {
                assertTrue(result.getProperties().isEmpty(), "Non empty properties for user: " + result.getName());
            } else if ("admin".equals(result.getName())) {
                assertFalse(result.getGroups().isEmpty(), "Empty groups");
                assertNotNull(result.getGroups().get("admins"), "No group");
                assertEquals(2, result.getProperties().size(), "Wrong number of properties for user: " + result.getName());
            } else {
                assertFalse(result.getGroups().isEmpty(), "Empty groups");
                assertNotNull(result.getGroups().get("users"), "No group");
                assertFalse(result.getProperties().isEmpty(), "Empty properties for user: " + result.getName());
            }
        }
    }

    /**
     * Test find all using pagination and restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingPaginationAndRestriction() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<User> result = dao.findAll(params, Restriction.with("role", RestrictionOperator.LIKE, "US%"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(3, result.getSize(), "Wrong page size");
        assertEquals(3, result.getResults().size(), "Wrong size of results");
        assertEquals(3, result.getTotalSize(), "Wrong total size of results");

        for (final User item : result.getResults()) {
            assertNotNull(item.getProperties(), "Null properties");

            if ("anonymous".equals(item.getName())) {
                assertTrue(item.getProperties().isEmpty(), "Non empty properties for user: " + item.getName());
            } else if ("admin".equals(item.getName())) {
                assertEquals(2, item.getProperties().size(), "Wrong number of properties for user: " + item.getName());
            } else {
                assertFalse(item.getProperties().isEmpty(), "Empty properties for user: " + item.getName());
            }
        }
    }

    /**
     * Test find by identifier.
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        final User expected = dao.findByName("admin");
        final User result = dao.findById(expected.getId()).get();

        assertNotNull(result, "Null result");
        assertEquals(expected.getId().longValue(), result.getId().longValue(), "Wrong id");
        assertEquals(expected.getConnectionsCount(), result.getConnectionsCount(), "Wrong connections count");
        assertEquals(expected.getCreationDate(), result.getCreationDate(), "Wrong creation date");
        assertEquals(expected.getExpirationDate(), result.getExpirationDate(), "Wrong expiration date");
        assertEquals(expected.getConnectionDate(), result.getConnectionDate(), "Wrong last connection date");
        assertEquals(expected.getModificationDate(), result.getModificationDate(), "Wrong modification date");
        assertEquals(expected.getDisplayName(), result.getDisplayName(), "Wrong display name");
        assertEquals(expected.getEmail(), result.getEmail(), "Wrong email");
        assertEquals(expected.getIp(), result.getIp(), "Wrong last IP");
        assertEquals(expected.getName(), result.getName(), "Wrong name");
        assertEquals(expected.getPassword(), result.getPassword(), "Wrong password");
        assertEquals(expected.getRole(), result.getRole(), "Wrong role");
        assertNotNull(result.getProperties(), "Wrong properties");
        assertEquals(expected.getProperties().size(), result.getProperties().size(), "Wrong count of properties");
        assertNotNull(result.getProperties().get("prop10"), "Wrong property");
        assertEquals("val10", result.getProperties().get("prop10").getValue(), "Wrong property");
        assertNotNull(result.getProperties().get("prop10").getLabel(), "Wrong property");
        assertNotNull(result.getProperties().get("prop11"), "Wrong property");
        assertEquals("val11", result.getProperties().get("prop11").getValue(), "Wrong property");
        assertNotNull(result.getProperties().get("prop11").getLabel(), "Wrong property");
    }

    /**
     * Test find by name.
     * @throws Exception the exception
     */
    @Test
    void testFindByName() throws Exception {
        final User result = dao.findByName("user1");

        assertNotNull(result, "Null result");
        assertEquals("user1", result.getName(), "Wrong name");
    }

    /**
     * Test find by property.
     * @throws Exception the exception
     */
    @Test
    void testFindByProperty() throws Exception {
        final List<User> results = dao.findByProperty("prop10", "val10");

        assertNotNull(results, "Null results");
        assertEquals(1, results.size(), "Null results wize");
        assertEquals("val10", results.get(0).getProperties().get("prop10").getValue(), "Wrong name");
    }

    /**
     * Test find by role.
     * @throws Exception the exception
     */
    @Test
    void testFindByRole() throws Exception {
        final Collection<User> results = dao.findByRole(Constants.USER_ROLE);

        assertNotNull(results, "Null result");
        assertEquals(3, results.size(), "Wrong number of elements");
        results.forEach(u -> assertEquals(Constants.USER_ROLE, u.getRole(), "Wrong result"));
    }

    /**
     * Test find links.
     * @throws Exception the exception
     */
    @Test
    void testFindLinks() throws Exception {
        final Page<ObjectLink> result = dao.findLinks(Pagination.UNUSED, null);

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(COUNT, result.getResults().size(), "Wrong number of elements");

        for (final ObjectLink link : result.getResults()) {
            assertNotNull(link.getId(), "Null id");
            assertNotNull(link.getLabel(), "Null label");
        }
    }

    /**
     * Test find without pagination.
     * @throws Exception the exception
     */
    @Test
    void testFindWithoutPagination() throws Exception {
        final Page<User> result = dao.findAll(Pagination.UNUSED, null);

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT, result.getTotalSize(), "Wrong total size");
        assertEquals(COUNT, result.getSize(), "Wrong size");
        assertEquals(COUNT, result.getResults().size(), "Wrong number of results");
    }

    /**
     * Test insert.
     * @throws Exception the exception
     */
    @Test
    void testInsert() throws Exception {
        final long count = dao.count();
        final User result = new User(dao.findByName("user1"));
        assertNotNull(result, "Null result");
        result.setId(null);
        result.setName("user4");
        result.setDisplayName("User 4");
        result.setEmail("user4@domain.com");
        result.getProperties().put("myprop1", new UserProperty("myprop1", PropertyType.STRING, "myvalue1"));
        result.getProperties().put("myprop2", new UserProperty("myprop2", PropertyType.INTEGER, "1"));
        result.getProperties().put("myprop3", new UserProperty("myprop3", PropertyType.STRING, "myvalue3"));
        result.getProperties().put("myprop4", new UserProperty("myprop4", PropertyType.STRING, "myvalue4"));
        result.getProperties().put("myprop5", new UserProperty("myprop5", PropertyType.STRING, "myvalue5"));
        result.getProperties().put("myprop6", new UserProperty("myprop6", PropertyType.STRING, "myvalue6"));

        dao.insert(result);

        final User inserted = dao.findById(result.getId()).get();
        assertNotNull(inserted, "Null result");
        assertNotNull(inserted.getId(), "Wrong id");
        assertEquals(0, inserted.getConnectionsCount(), "Wrong connections count");
        assertNotNull(inserted.getCreationDate(), "Wrong creation date");
        assertNull(inserted.getExpirationDate(), "Wrong expiration date");
        assertNull(inserted.getConnectionDate(), "Wrong last connection date");
        assertNotNull(inserted.getModificationDate(), "Wrong modification date");
        assertEquals(result.getDisplayName(), inserted.getDisplayName(), "Wrong display name");
        assertEquals(result.getEmail(), inserted.getEmail(), "Wrong email");
        assertNull(inserted.getIp(), "Wrong last IP");
        assertEquals(result.getName(), inserted.getName(), "Wrong name");
        assertEquals(result.getPassword(), inserted.getPassword(), "Wrong password");
        assertEquals(result.getRole(), inserted.getRole(), "Wrong role");
        assertNotNull(inserted.getGroups(), "Null groups");
        assertEquals(result.getGroups().keySet(), inserted.getGroups().keySet(), "Wrong groups");
        assertEquals(count + 1, dao.count(), "Wrong count");
        assertNotNull(inserted.getProperties(), "Wrong properties");
        assertEquals(7, inserted.getProperties().size(), "Wrong count of properties");
        assertNotNull(inserted.getProperties().get("myprop1"), "Wrong property");
        assertEquals("myvalue1", inserted.getProperties().get("myprop1").getValue(), "Wrong property");
    }

    /**
     * Test update.
     * @throws Exception the exception
     */
    @Test
    void testUpdate() throws Exception {
        final long count = dao.count();
        final User result = dao.findByName("user1");
        assertNotNull(result, "Null result");
        result.setName("user999");
        result.setDisplayName("User 999");
        result.setEmail("use999@domain.com");
        result.getProperties().put("myprop1", new UserProperty("myprop1", PropertyType.STRING, "myvalue2"));
        result.getProperties().put("myprop2", new UserProperty("myprop2", PropertyType.STRING, "myvalue2"));

        dao.update(result);

        final User updated = dao.findByName("user999");
        assertNotNull(updated, "Null result");
        assertNotNull(updated.getId(), "Wrong id");
        assertEquals(result.getConnectionsCount(), updated.getConnectionsCount(), "Wrong connections count");
        assertEquals(result.getCreationDate(), updated.getCreationDate(), "Wrong creation date");
        assertEquals(result.getExpirationDate(), updated.getExpirationDate(), "Wrong expiration date");
        assertEquals(result.getConnectionDate(), updated.getConnectionDate(), "Wrong last connection date");
        assertNotNull(updated.getModificationDate(), "Wrong modification date");
        assertEquals(result.getModificationDate().getTime(), updated.getModificationDate().getTime(), "Wrong modification date");
        assertEquals(result.getDisplayName(), updated.getDisplayName(), "Wrong display name");
        assertEquals(result.getEmail(), updated.getEmail(), "Wrong email");
        assertEquals(result.getIp(), updated.getIp(), "Wrong last IP");
        assertEquals(result.getName(), updated.getName(), "Wrong name");
        assertEquals(result.getPassword(), updated.getPassword(), "Wrong password");
        assertEquals(result.getRole(), updated.getRole(), "Wrong role");
        assertEquals(count, dao.count(), "Wrong count");
        assertNotNull(updated.getProperties(), "Wrong properties");
        assertEquals(3, updated.getProperties().size(), "Wrong count of properties");
        assertNotNull(updated.getProperties().get("myprop1"), "Wrong property");
        assertEquals("myvalue2", updated.getProperties().get("myprop1").getValue(), "Wrong property");
        assertNotNull(updated.getProperties().get("myprop2"), "Wrong property");
        assertEquals("myvalue2", updated.getProperties().get("myprop2").getValue(), "Wrong property");
    }
}
