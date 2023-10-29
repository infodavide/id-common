package org.infodavid.commons.persistence.impl.springdata.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.infodavid.commons.model.ApplicationSetting;
import org.infodavid.commons.model.Page;
import org.infodavid.commons.model.query.Pagination;
import org.infodavid.commons.model.query.Restriction;
import org.infodavid.commons.model.query.RestrictionOperator;
import org.infodavid.commons.persistence.dao.ApplicationSettingDao;
import org.infodavid.commons.persistence.impl.springdata.AbstractSpringTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import jakarta.persistence.PersistenceException;

/**
 * The Class ApplicationSettingRepositoryTest.
 */
@Sql({
        "/application_settings.sql"
})
class ApplicationSettingRepositoryTest extends AbstractSpringTest {

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
    void testCount() throws Exception {
        assertEquals(COUNT, dao.count(), "Wrong result");
    }

    /**
     * Test delete by identifier.
     * @throws Exception the exception
     */
    @Test
    void testDeleteById() throws Exception {
        dao.deleteById(dao.findByName("Param2").getId());

        final Optional<ApplicationSetting> optional = dao.findById(Long.valueOf(2));

        assertNotNull(optional, "Null result");
        assertTrue(optional.isEmpty(), "No result");
    }

    /**
     * Test delete by identifier.
     * @throws Exception the exception
     */
    @Test
    void testDeleteByName() throws Exception {
        dao.deleteByName("Param2");

        final ApplicationSetting result = dao.findByName("Param2");

        assertNull(result, "Null result");
    }

    /**
     * Test find all.
     * @throws Exception the exception
     */
    @Test
    void testFindAll() throws Exception {
        final Iterable<ApplicationSetting> results = dao.findAll();

        assertNotNull(results, "Null result");
        assertEquals(COUNT, size(results), "Wrong number of elements");
    }

    /**
     * Test find all using equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.EQ, "Scope1"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using greater or equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingGreaterOrEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.GE, Double.valueOf(-64.0)));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(7, result.getSize(), "Wrong page size");
        assertEquals(7, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using greater than restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingGreaterThanRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("minimum", RestrictionOperator.GT, Double.valueOf(-64.0)));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(6, result.getSize(), "Wrong page size");
        assertEquals(6, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using insensitive equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_EQ, "scope1"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using insensitive equals restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveEqualsRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.INSENSITIVE_EQ, "scope1");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using insensitive like restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveLikeRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_LIKE, "scope%"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using insensitive like restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveLikeRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.INSENSITIVE_LIKE, "scope%");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using insensitive not equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveNotEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_NOT_EQ, "scope1"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT - 1, result.getSize(), "Wrong page size");
        assertEquals(COUNT - 1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using insensitive not equals restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveNotEqualsRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.INSENSITIVE_NOT_EQ, "scope1");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using insensitive not like restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveNotLikeRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.INSENSITIVE_NOT_LIKE, "scope%"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT - 1, result.getSize(), "Wrong page size");
        assertEquals(COUNT - 1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using insensitive not like restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingInsensitiveNotLikeRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.INSENSITIVE_NOT_LIKE, "scope%");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using less or equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingLessOrEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("maximum", RestrictionOperator.LE, Double.valueOf(64.0)));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(5, result.getSize(), "Wrong page size");
        assertEquals(5, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using less than restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingLessThanRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("maximum", RestrictionOperator.LT, Double.valueOf(64.0)));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(3, result.getSize(), "Wrong page size");
        assertEquals(3, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using like restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingLikeRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.LIKE, "Scope%"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using like restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingLikeRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.LIKE, "Scope%");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using not equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingNotEqualsRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_EQ, "Scope1"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT - 1, result.getSize(), "Wrong page size");
        assertEquals(COUNT - 1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using not like restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingNotLikeRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_LIKE, "Scope%"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT - 1, result.getSize(), "Wrong page size");
        assertEquals(COUNT - 1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using not like restriction with number.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingNotLikeRestrictionWithNumber() throws Exception {
        final Restriction restriction = Restriction.with("minimum", RestrictionOperator.NOT_LIKE, "Scope%");

        assertThrows(PersistenceException.class, () -> dao.findAll(Pagination.UNUSED, restriction));
    }

    /**
     * Test find all using not null restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingNotNullRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NOT_NULL));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using pagination.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingPagination() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<ApplicationSetting> result = dao.findAll(params, null);

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT, result.getTotalSize(), "Wrong total size");
        assertEquals(params.getSize(), result.getSize(), "Wrong page size");
        assertEquals(params.getSize(), result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all using pagination and equals restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsingPaginationAndEqualsRestriction() throws Exception {
        final Pagination params = new Pagination();
        params.setNumber(1);
        params.setSize(5);

        final Page<ApplicationSetting> result = dao.findAll(params, Restriction.with("scope", RestrictionOperator.EQ, "Scope1"));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(1, result.getSize(), "Wrong page size");
        assertEquals(1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all usin null restriction.
     * @throws Exception the exception
     */
    @Test
    void testFindAllUsinNullRestriction() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, Restriction.with("scope", RestrictionOperator.NULL));

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT - 1, result.getSize(), "Wrong page size");
        assertEquals(COUNT - 1, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find all without pagination.
     * @throws Exception the exception
     */
    @Test
    void testFindAllWithoutPagination() throws Exception {
        final Page<ApplicationSetting> result = dao.findAll(Pagination.UNUSED, null);

        assertNotNull(result, "Null result");
        assertNotNull(result.getResults(), "Null results");
        assertEquals(1, result.getNumber(), "Wrong page number");
        assertEquals(COUNT, result.getSize(), "Wrong page size");
        assertEquals(COUNT, result.getResults().size(), "Wrong size of results");
    }

    /**
     * Test find by identifier.
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        final ApplicationSetting expected = dao.findByName("Param1");
        final Optional<ApplicationSetting> optional = dao.findById(expected.getId());

        assertNotNull(optional, "Null result");
        assertTrue(optional.isPresent(), "No result");
        final ApplicationSetting result = optional.get();
        assertNotNull(result, "Null result");
        assertEquals(expected.getId().longValue(), result.getId().longValue(), "Wrong id");
        assertEquals(expected.getType(), result.getType(), "Wrong type");
        assertEquals(expected.getCreationDate(), result.getCreationDate(), "Wrong creation date");
        assertEquals(expected.getModificationDate(), result.getModificationDate(), "Wrong modification date");
        assertEquals(expected.getLabel(), result.getLabel(), "Wrong label");
        assertEquals(expected.getValue(), result.getValue(), "Wrong value");
    }

    /**
     * Test find by name.
     * @throws Exception the exception
     */
    @Test
    void testFindByName() throws Exception {
        final ApplicationSetting result = dao.findByName("Param2");

        assertNotNull(result, "Null result");
        assertEquals("Donnée2", result.getValue(), "Wrong value");
        assertEquals("Param2", result.getName(), "Wrong name");
    }

    /**
     * Test find by scope.
     * @throws Exception the exception
     */
    @Test
    void testFindByScope() throws Exception {
        final List<ApplicationSetting> results = dao.findByScope("Scope1");

        assertNotNull(results, "Null result");
        assertEquals(1, results.size(), "Wrong id");

        for (final ApplicationSetting ApplicationSetting : results) {
            assertEquals("Scope1", ApplicationSetting.getScope(), "Wrong scope");
        }

        assertEquals("Param3", results.get(0).getName(), "Wrong name");
    }

    /**
     * Test insert.
     * @throws Exception the exception
     */
    @Test
    void testInsert() throws Exception {
        final long count = dao.count();
        final ApplicationSetting result = new ApplicationSetting(dao.findByName("Param2"));
        assertNotNull(result, "Null result");
        result.setId(null);
        result.setName("param33");

        dao.insert(result);

        final ApplicationSetting inserted = dao.findById(result.getId()).get();
        assertNotNull(inserted, "Null result");
        assertNotNull(inserted.getId(), "Wrong id");
        assertNotNull(inserted.getCreationDate(), "Wrong creation date");
        assertNotNull(result.getLabel(), "Wrong label");
        assertEquals(result.getName(), inserted.getName(), "Wrong name");
        assertEquals(result.getType(), inserted.getType(), "Wrong type");
        assertEquals(result.getValue(), inserted.getValue(), "Wrong value");
        assertEquals(count + 1, dao.count(), "Wrong count");
    }

    /**
     * Test update.
     * @throws Exception the exception
     */
    @Test
    void testUpdate() throws Exception {
        final long count = dao.count();
        final ApplicationSetting result = dao.findByName("Param2");
        assertNotNull(result, "Null result");
        result.setName("param999");

        dao.update(result);

        final ApplicationSetting updated = dao.findByName("param999");
        assertNotNull(updated, "Null result");
        assertEquals(result.getId(), updated.getId(), "Wrong id");
        assertEquals(result.getType(), updated.getType(), "Wrong type");
        assertEquals(result.getCreationDate(), updated.getCreationDate(), "Wrong creation date");
        assertNotNull(updated.getModificationDate(), "Wrong modification date");
        assertNotNull(result.getLabel(), "Wrong label");
        assertEquals(result.getValue(), updated.getValue(), "Wrong value");
        assertEquals(result.getName(), updated.getName(), "Wrong name");
        assertEquals(count, dao.count(), "Wrong count");
    }
}
