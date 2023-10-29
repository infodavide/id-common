package org.infodavid.commons.system;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.attribute.UserPrincipal;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * The Class SystemUtilitiesTest.
 */
@SuppressWarnings("static-method")
class SystemUtilitiesTest extends TestCase {

    /**
     * Test get architecture.
     * @throws Exception the exception
     */
    @Test
    void testGetArchitecture() throws Exception {
        final String result = SystemUtilities.getInstance().getArchitecture();

        assertNotNull(result, "Wrong result");
        assertFalse(StringUtils.isEmpty(result), "Wrong result");
    }

    /**
     * Test get available time zones.
     * @throws Exception the exception
     */
    @Test
    void testGetAvailableTimeZones() throws Exception {
        final String[] result = SystemUtilities.getInstance().getAvailableTimeZones();

        assertNotNull(result, "Wrong result");
        assertNotEquals(0, result.length, "Wrong result");
    }

    /**
     * Test get group principal.
     * @throws Exception the exception
     */
    @Test
    void testGetGroupPrincipal() throws Exception {
        final String name;

        if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            if (Locale.getDefault().getISO3Language().equals(Locale.FRANCE.getISO3Language())) {
                name = "Utilisateurs";
            } else {
                name = "Users";
            }
        } else {
            name = "users";
        }

        final UserPrincipal result = SystemUtilities.getInstance().getGroupPrincipal(name);

        assertNotNull(result, "Wrong result");
    }

    /**
     * Test get name.
     * @throws Exception the exception
     */
    @Test
    void testGetName() throws Exception {
        final String result = SystemUtilities.getInstance().getName();

        assertNotNull(result, "Wrong result");
        assertFalse(StringUtils.isEmpty(result), "Wrong result");
    }

    /**
     * Test get time zone.
     * @throws Exception the exception
     */
    @Test
    void testGetTimeZone() throws Exception {
        final String result = SystemUtilities.getInstance().getTimeZone();

        assertNotNull(result, "Wrong result");
        assertFalse(StringUtils.isEmpty(result), "Wrong result");
    }

    /**
     * Test get user name.
     * @throws Exception the exception
     */
    @Test
    void testGetUserName() throws Exception {
        final String result = SystemUtilities.getInstance().getUserName();

        assertNotNull(result, "Wrong result");
        assertFalse(StringUtils.isEmpty(result), "Wrong result");
    }

    /**
     * Test get user principal.
     * @throws Exception the exception
     */
    @Test
    void testGetUserPrincipal() throws Exception {
        final UserPrincipal result = SystemUtilities.getInstance().getUserPrincipal();

        assertNotNull(result, "Wrong result");
    }

    /**
     * Test get version.
     * @throws Exception the exception
     */
    @Test
    void testGetVersion() throws Exception {
        final String result = SystemUtilities.getInstance().getVersion();

        assertNotNull(result, "Wrong result");
        assertFalse(StringUtils.isEmpty(result), "Wrong result");
    }
}
