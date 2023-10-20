package org.infodavid.commons.persistence.impl.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.Constants;
import org.infodavid.commons.model.EntityReference;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.PropertyType;
import org.infodavid.commons.model.UserGroup;
import org.infodavid.commons.model.UserGroupProperty;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.model.query.RestrictionOperator;
import org.infodavid.commons.persistence.dao.UsersGroupDao;
import org.infodavid.commons.persistence.impl.AbstractSpringTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

/**
 * The Class UserGroupRepositoryTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Sql({
        "/user_groups.sql"
})
public class UserGroupRepositoryTest extends AbstractSpringTest {

    /** The Constant COUNT. */
    private static final byte COUNT = 2;

    /** The data access object. */
    @Autowired
    private UsersGroupDao dao;

    /**
     * Test count.
     * @throws Exception the exception
     */
    @Test
    public void testCount() throws Exception {
        assertEquals("Wrong result", COUNT, dao.count());
    }

    /**
     * Test delete by id.
     * @throws Exception the exception
     */
    @Test
    public void testDeleteById() throws Exception {
        dao.deleteById(Long.valueOf(2));

        final Optional<UserGroup> optional = dao.findById(Long.valueOf(2));

        assertNotNull("Null result", optional);
        assertTrue("No result", optional.isEmpty());
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    public void testFindAll() throws Exception {
        final Iterable<UserGroup> results = dao.findAll();

        assertNotNull("Null result", results);
        assertEquals("Wrong number of elements", COUNT, size(results));
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingPagination() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(2);

        final Page<UserGroup> results = dao.findAll(params, null);

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT, results.getTotalSize());
        assertEquals("Wrong results", params.getSize(), results.getSize());
        assertEquals("Wrong results", params.getSize(), results.getResults().size());

        for (final UserGroup result : results.getResults()) {
            assertNotNull("Null properties", result.getProperties());

            if ("anonymous".equals(result.getName())) {
                assertTrue("Non empty properties for group: " + result.getName(), result.getProperties().isEmpty());
            } else if ("admin".equals(result.getName())) {
                assertEquals("Wrong number of properties for group: " + result.getName(), 2, result.getProperties().size());
            } else {
                assertFalse("Empty properties for group: " + result.getName(), result.getProperties().isEmpty());
            }
        }
    }

    /**
     * Test find all using pagination and restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingPaginationAndRestriction() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<UserGroup> results = dao.findAll(params, Restriction.with("role", RestrictionOperator.LIKE, "US%"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getTotalSize());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());

        for (final UserGroup result : results.getResults()) {
            assertNotNull("Null properties", result.getProperties());

            if ("anonymous".equals(result.getName())) {
                assertTrue("Non empty properties for group: " + result.getName(), result.getProperties().isEmpty());
            } else if ("admin".equals(result.getName())) {
                assertEquals("Wrong number of properties for group: " + result.getName(), 2, result.getProperties().size());
            } else {
                assertFalse("Empty properties for group: " + result.getName(), result.getProperties().isEmpty());
            }
        }
    }

    /**
     * Test find by identifier.
     * @throws Exception the exception
     */
    @Test
    public void testFindById() throws Exception {
        final UserGroup expected = dao.findByName("admins");
        final UserGroup result = dao.findById(expected.getId()).get();

        assertNotNull("Null result", result);
        assertEquals("Wrong id", expected.getId().longValue(), result.getId().longValue());
        assertEquals("Wrong creation date", expected.getCreationDate(), result.getCreationDate());
        assertEquals("Wrong modification date", expected.getModificationDate(), result.getModificationDate());
        assertEquals("Wrong display name", expected.getDisplayName(), result.getDisplayName());
        assertEquals("Wrong name", expected.getName(), result.getName());
        assertEquals("Wrong role", expected.getRole(), result.getRole());
        assertNotNull("Wrong properties", result.getProperties());
        assertEquals("Wrong count of properties", expected.getProperties().size(), result.getProperties().size());
        assertNotNull("Wrong property", result.getProperties().get("prop10"));
        assertEquals("Wrong property", "val10", result.getProperties().get("prop10").getValue());
        assertNotNull("Wrong property", result.getProperties().get("prop10").getLabel());
        assertNotNull("Wrong property", result.getProperties().get("prop11"));
        assertEquals("Wrong property", "val11", result.getProperties().get("prop11").getValue());
        assertNotNull("Wrong property", result.getProperties().get("prop11").getLabel());
    }

    /**
     * Test find by name.
     * @throws Exception the exception
     */
    @Test
    public void testFindByName() throws Exception {
        final UserGroup result = dao.findByName("users");

        assertNotNull("Null result", result);
        assertEquals("Wrong name", "users", result.getName());
    }

    /**
     * Test find by property.
     * @throws Exception the exception
     */
    @Test
    public void testFindByProperty() throws Exception {
        final List<UserGroup> results = dao.findByProperty("prop10", "val10");

        assertNotNull("Null results", results);
        assertEquals("Null results wize", 1, results.size());
        assertEquals("Wrong name", "val10", results.get(0).getProperties().get("prop10").getValue());
    }

    /**
     * Test find by role.
     * @throws Exception the exception
     */
    @Test
    public void testFindByRole() throws Exception {
        final Collection<UserGroup> results = dao.findByRole(Constants.USER_ROLE);

        assertNotNull("Null result", results);
        assertEquals("Wrong number of elements", 1, results.size());
        results.forEach(u -> assertEquals("Wrong result", Constants.USER_ROLE, u.getRole()));
    }

    /**
     * Test find references.
     * @throws Exception the exception
     */
    @Test
    public void testFindReferences() throws Exception {
        final Page<EntityReference> result = dao.findReferences(Pagination.UNUSED, null);

        assertNotNull("Null result", result);
        assertNotNull("Null result", result.getResults());
        assertEquals("Wrong number of elements", COUNT, result.getResults().size());

        for (final EntityReference reference : result.getResults()) {
            assertNotNull("Null id", reference.getId());
            assertNotNull("Null label", reference.getLabel());
        }
    }

    /**
     * Test find without pagination.
     * @throws Exception the exception
     */
    @Test
    public void testFindWithoutPagination() throws Exception {
        final Page<UserGroup> results = dao.findAll(Pagination.UNUSED, null);

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT, results.getTotalSize());
        assertEquals("Wrong results", COUNT, results.getSize());
        assertEquals("Wrong results", COUNT, results.getResults().size());
    }

    /**
     * Test insert.
     * @throws Exception the exception
     */
    @Test
    public void testInsert() throws Exception {
        final long count = dao.count();
        final UserGroup result = new UserGroup(dao.findByName("users"));
        assertNotNull("Null result", result);
        result.setId(null);
        result.setName("group3");
        result.setDisplayName("Group 3");
        result.getProperties().put("myprop1", new UserGroupProperty("myprop1", PropertyType.STRING, "myvalue1"));
        result.getProperties().put("myprop2", new UserGroupProperty("myprop2", PropertyType.INTEGER, "1"));
        result.getProperties().put("myprop3", new UserGroupProperty("myprop3", PropertyType.STRING, "myvalue3"));
        result.getProperties().put("myprop4", new UserGroupProperty("myprop4", PropertyType.STRING, "myvalue4"));
        result.getProperties().put("myprop5", new UserGroupProperty("myprop5", PropertyType.STRING, "myvalue5"));
        result.getProperties().put("myprop6", new UserGroupProperty("myprop6", PropertyType.STRING, "myvalue6"));

        dao.insert(result);

        final UserGroup inserted = dao.findById(result.getId()).get();
        assertNotNull("Null result", inserted);
        assertNotNull("Wrong id", inserted.getId());
        assertNotNull("Wrong creation date", inserted.getCreationDate());
        assertNotNull("Wrong modification date", inserted.getModificationDate());
        assertEquals("Wrong display name", result.getDisplayName(), inserted.getDisplayName());
        assertEquals("Wrong name", result.getName(), inserted.getName());
        assertEquals("Wrong role", result.getRole(), inserted.getRole());
        assertEquals("Wrong count", count + 1, dao.count());
        assertNotNull("Wrong properties", inserted.getProperties());
        assertEquals("Wrong count of properties", 9, inserted.getProperties().size());
        assertNotNull("Wrong property", inserted.getProperties().get("myprop1"));
        assertEquals("Wrong property", "myvalue1", inserted.getProperties().get("myprop1").getValue());
    }

    /**
     * Test update.
     * @throws Exception the exception
     */
    @Test
    public void testUpdate() throws Exception {
        final long count = dao.count();
        final UserGroup result = dao.findByName("users");
        assertNotNull("Null result", result);
        result.setName("group999");
        result.setDisplayName("Group 999");
        result.getProperties().put("myprop1", new UserGroupProperty("myprop1", PropertyType.STRING, "myvalue2"));
        result.getProperties().put("myprop2", new UserGroupProperty("myprop2", PropertyType.STRING, "myvalue2"));

        dao.update(result);

        final UserGroup updated = dao.findByName("group999");
        assertNotNull("Null result", updated);
        assertNotNull("Wrong id", updated.getId());
        assertEquals("Wrong creation date", result.getCreationDate(), updated.getCreationDate());
        assertNotNull("Wrong modification date", updated.getModificationDate());
        assertEquals("Wrong modification date", result.getModificationDate().getTime(), updated.getModificationDate().getTime());
        assertEquals("Wrong display name", result.getDisplayName(), updated.getDisplayName());
        assertEquals("Wrong name", result.getName(), updated.getName());
        assertEquals("Wrong role", result.getRole(), updated.getRole());
        assertEquals("Wrong count", count, dao.count());
        assertNotNull("Wrong properties", updated.getProperties());
        assertEquals("Wrong count of properties", 5, updated.getProperties().size());
        assertNotNull("Wrong property", updated.getProperties().get("myprop1"));
        assertEquals("Wrong property", "myvalue2", updated.getProperties().get("myprop1").getValue());
        assertNotNull("Wrong property", updated.getProperties().get("myprop2"));
        assertEquals("Wrong property", "myvalue2", updated.getProperties().get("myprop2").getValue());
    }
}
