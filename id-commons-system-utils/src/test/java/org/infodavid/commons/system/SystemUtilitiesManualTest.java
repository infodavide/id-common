package org.infodavid.commons.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * The Class SystemUtilitiesManualTest.
 */
@SuppressWarnings("static-method")
class SystemUtilitiesManualTest extends TestCase {

    /**
     * Test set time.
     * @throws Exception the exception
     */
    @Test
    void testSetDateTime() throws Exception {
        final ZonedDateTime previous = SystemUtilities.getInstance().getDateTime();
        assertNotNull(previous, "Wrong result");

        try {
            final ZonedDateTime expected = previous.minusDays(1);

            SystemUtilities.getInstance().setDateTime(expected);

            final ZonedDateTime current = SystemUtilities.getInstance().getDateTime();

            assertTrue(current.isAfter(expected.minusSeconds(10)), "Wrong result");
            assertTrue(current.isBefore(expected.plusSeconds(10)), "Wrong result");
        } finally {
            SystemUtilities.getInstance().setDateTime(previous);
        }
    }

    /**
     * Test set time zone.
     * @throws Exception the exception
     */
    @Test
    void testSetTimeZone() throws Exception {
        final String previous = SystemUtilities.getInstance().getTimeZone();
        assertNotNull(previous, "Wrong result");
        assertFalse(StringUtils.isEmpty(previous), "Wrong result");

        try {
            final String expected = "America/New_York";
            SystemUtilities.getInstance().setTimeZone(expected);

            final String tz = SystemUtilities.getInstance().getTimeZone();

            assertEquals(expected, tz, "Wrong result");
        } finally {
            SystemUtilities.getInstance().setTimeZone(previous);
        }
    }

    /**
     * Test set time zone.
     * @throws Exception the exception
     */
    @Test
    void testSetTimeZoneWithInvalid() throws Exception {
        final SystemUtilities utils = SystemUtilities.getInstance();

        assertThrows(IllegalArgumentException.class, () -> utils.setTimeZone(null));
        assertThrows(IllegalArgumentException.class, () -> utils.setTimeZone(""));
    }
}
