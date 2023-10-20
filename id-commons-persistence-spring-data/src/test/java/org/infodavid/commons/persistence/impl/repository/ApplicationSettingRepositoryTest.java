package org.infodavid.commons.persistence.impl.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.ApplicationSetting;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.model.query.RestrictionOperator;
import org.infodavid.commons.persistence.dao.ApplicationSettingDao;
import org.infodavid.commons.persistence.impl.AbstractSpringTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import jakarta.persistence.PersistenceException;

/**
 * The Class ApplicationSettingRepositoryTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Sql({
        "/application_settings.sql"
})
public class ApplicationSettingRepositoryTest extends AbstractSpringTest {

    /** The Constant COUNT. */
    private static final long COUNT = 19L;

    /** The data access object. */
    @Autowired
    private ApplicationSettingDao dao;

    /**
     * Test count.
     * @throws Exception the exception
     */
    @Test
    public void testCount() throws Exception {
        assertEquals("Wrong result", COUNT, dao.count());
    }

    /**
     * Test delete by identifier.
     * @throws Exception the exception
     */
    @Test
    public void testDeleteById() throws Exception {
        dao.deleteById(dao.findByName("Param2").getId());

        final Optional<ApplicationSetting> optional = dao.findById(Long.valueOf(2));

        assertNotNull("Null result", optional);
        assertTrue("No result", optional.isEmpty());
    }

    /**
     * Test delete by identifier.
     * @throws Exception the exception
     */
    @Test
    public void testDeleteByName() throws Exception {
        dao.deleteByName("Param2");

        final ApplicationSetting result = dao.findByName("Param2");

        assertNull("Null result", result);
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    public void testFindAll() throws Exception {
        final Iterable<ApplicationSetting> results = dao.findAll();

        assertNotNull("Null result", results);
        assertEquals("Wrong number of elements", COUNT, size(results));
    }

    /**
     * Test find all using equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.EQ, "Scope1"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all using greater or equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingGreaterOrEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.GE, Double.valueOf(-64.0)));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 7, results.getSize());
        assertEquals("Wrong results", 7, results.getResults().size());
    }

    /**
     * Test find all using greater than restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingGreaterThanRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.GT, Double.valueOf(-64.0)));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 6, results.getSize());
        assertEquals("Wrong results", 6, results.getResults().size());
    }

    /**
     * Test find all using insensitive equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_EQ, "scope1"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all using insensitive equals restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveEqualsRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.INSENSITIVE_EQ, "scope1")));
    }

    /**
     * Test find all using insensitive like restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveLikeRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_LIKE, "scope%"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all using insensitive like restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveLikeRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.INSENSITIVE_LIKE, "scope%")));
    }

    /**
     * Test find all using insensitive not equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveNotEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_NOT_EQ, "scope1"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT - 1, results.getSize());
        assertEquals("Wrong results", COUNT - 1, results.getResults().size());
    }

    /**
     * Test find all using insensitive not equals restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveNotEqualsRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.INSENSITIVE_NOT_EQ, "scope1")));
    }

    /**
     * Test find all using insensitive not like restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveNotLikeRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_NOT_LIKE, "scope%"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT - 1, results.getSize());
        assertEquals("Wrong results", COUNT - 1, results.getResults().size());
    }

    /**
     * Test find all using insensitive not like restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingInsensitiveNotLikeRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.INSENSITIVE_NOT_LIKE, "scope%")));
    }

    /**
     * Test find all using less or equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingLessOrEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("maximum", RestrictionOperator.LE, Double.valueOf(64.0)));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 5, results.getSize());
        assertEquals("Wrong results", 5, results.getResults().size());
    }

    /**
     * Test find all using less than restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingLessThanRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("maximum", RestrictionOperator.LT, Double.valueOf(64.0)));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 3, results.getSize());
        assertEquals("Wrong results", 3, results.getResults().size());
    }

    /**
     * Test find all using like restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingLikeRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.LIKE, "Scope%"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all using like restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingLikeRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.LIKE, "Scope%")));
    }

    /**
     * Test find all using not equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingNotEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_EQ, "Scope1"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT - 1, results.getSize());
        assertEquals("Wrong results", COUNT - 1, results.getResults().size());
    }

    /**
     * Test find all using not like restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingNotLikeRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_LIKE, "Scope%"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT - 1, results.getSize());
        assertEquals("Wrong results", COUNT - 1, results.getResults().size());
    }

    /**
     * Test find all using not like restriction with number.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingNotLikeRestrictionWithNumber() throws Exception {
        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.NOT_LIKE, "Scope%")));
    }

    /**
     * Test find all using not null restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingNotNullRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_NULL));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all using pagination.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingPagination() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<ApplicationSetting> results = dao.findAll(params, null);

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT, results.getTotalSize());
        assertEquals("Wrong results", params.getSize(), results.getSize());
        assertEquals("Wrong results", params.getSize(), results.getResults().size());
    }

    /**
     * Test find all using pagination and equals restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsingPaginationAndEqualsRestriction() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<ApplicationSetting> results = dao.findAll(params, Restriction.with("scope", RestrictionOperator.EQ, "Scope1"));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", 1, results.getSize());
        assertEquals("Wrong results", 1, results.getResults().size());
    }

    /**
     * Test find all usin null restriction.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllUsinNullRestriction() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NULL));

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT - 1, results.getSize());
        assertEquals("Wrong results", COUNT - 1, results.getResults().size());
    }

    /**
     * Test find all without pagination.
     * @throws Exception the exception
     */
    @Test
    public void testFindAllWithoutPagination() throws Exception {
        final Page<ApplicationSetting> results = dao.findAll(Pagination.UNUSED, null);

        assertNotNull("Null results", results);
        assertNotNull("Null results", results.getResults());
        assertEquals("Wrong results", 1, results.getNumber());
        assertEquals("Wrong results", COUNT, results.getSize());
        assertEquals("Wrong results", COUNT, results.getTotalSize());
        assertEquals("Wrong results", COUNT, results.getResults().size());
    }

    /**
     * Test find by identifier.
     * @throws Exception the exception
     */
    @Test
    public void testFindById() throws Exception {
        final ApplicationSetting expected = dao.findByName("Param1");
        final Optional<ApplicationSetting> optional = dao.findById(expected.getId());

        assertNotNull("Null result", optional);
        assertTrue("No result", optional.isPresent());
        final ApplicationSetting result = optional.get();
        assertNotNull("Null result", result);
        assertEquals("Wrong id", expected.getId().longValue(), result.getId().longValue());
        assertEquals("Wrong type", expected.getType(), result.getType());
        assertEquals("Wrong creation date", expected.getCreationDate(), result.getCreationDate());
        assertEquals("Wrong creation date", expected.getModificationDate(), result.getModificationDate());
        assertEquals("Wrong label", expected.getLabel(), result.getLabel());
        assertEquals("Wrong value", expected.getValue(), result.getValue());
    }

    /**
     * Test find by name.
     * @throws Exception the exception
     */
    @Test
    public void testFindByName() throws Exception {
        final ApplicationSetting result = dao.findByName("Param2");

        assertNotNull("Null result", result);
        assertEquals("Wrong value", "Donnée2", result.getValue());
        assertEquals("Wrong name", "Param2", result.getName());
    }

    /**
     * Test find by scope.
     * @throws Exception the exception
     */
    @Test
    public void testFindByScope() throws Exception {
        final List<ApplicationSetting> results = dao.findByScope("Scope1");

        assertNotNull("Null result", results);
        assertEquals("Wrong id", 1, results.size());

        for (final ApplicationSetting ApplicationSetting : results) {
            assertEquals("Wrong scope", "Scope1", ApplicationSetting.getScope());
        }

        assertEquals("Wrong name", "Param3", results.get(0).getName());
    }

    /**
     * Test insert.
     * @throws Exception the exception
     */
    @Test
    public void testInsert() throws Exception {
        final long count = dao.count();
        final ApplicationSetting result = new ApplicationSetting(dao.findByName("Param2"));
        assertNotNull("Null result", result);
        result.setId(null);
        result.setName("param33");

        dao.insert(result);

        final ApplicationSetting inserted = dao.findById(result.getId()).get();
        assertNotNull("Null result", inserted);
        assertNotNull("Wrong id", inserted.getId());
        assertNotNull("Wrong creation date", inserted.getCreationDate());
        assertNotNull("Wrong label", result.getLabel());
        assertEquals("Wrong name", result.getName(), inserted.getName());
        assertEquals("Wrong type", result.getType(), inserted.getType());
        assertEquals("Wrong value", result.getValue(), inserted.getValue());
        assertEquals("Wrong count", count + 1, dao.count());
    }

    /**
     * Test update.
     * @throws Exception the exception
     */
    @Test
    public void testUpdate() throws Exception {
        final long count = dao.count();
        final ApplicationSetting result = dao.findByName("Param2");
        assertNotNull("Null result", result);
        result.setName("param999");

        dao.update(result);

        final ApplicationSetting updated = dao.findByName("param999");
        assertNotNull("Null result", updated);
        assertEquals("Wrong id", result.getId(), updated.getId());
        assertEquals("Wrong type", result.getType(), updated.getType());
        assertEquals("Wrong creation date", result.getCreationDate(), updated.getCreationDate());
        assertNotNull("Wrong label", result.getLabel());
        assertEquals("Wrong value", result.getValue(), updated.getValue());
        assertEquals("Wrong name", result.getName(), updated.getName());
        assertNotNull("Wrong modification date", updated.getModificationDate());
        assertEquals("Wrong count", count, dao.count());
    }
}
