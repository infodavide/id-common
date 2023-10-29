package org.infodavid.commons.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * The Class NetworkUtilitiesTest.
 */
@SuppressWarnings("static-method")
class NetworkUtilitiesManualTest extends TestCase {

    /**
     * Test set DNS servers.
     * @throws Exception the exception
     */
    @Test
    void testSetDnsServersWithInvalid() throws Exception {
        final NetworkUtilities utils = NetworkUtilities.getInstance();
        assertThrows(IllegalArgumentException.class, () -> utils.setDnsServers(null, (String[]) null));
        assertThrows(IllegalArgumentException.class, () -> utils.setDnsServers("", (String[]) null));
        assertThrows(IllegalArgumentException.class, () -> utils.setDnsServers("intranet", (String) null));
        assertThrows(IllegalArgumentException.class, () -> utils.setDnsServers("intranet", ""));
    }

    /**
     * Test set host name.
     * @throws Exception the exception
     */
    @Test
    void testSetHostNameWithInvalid() throws Exception {
        final NetworkUtilities utils = NetworkUtilities.getInstance();
        assertThrows(IllegalArgumentException.class, () -> utils.setHostName(null));
        assertThrows(IllegalArgumentException.class, () -> utils.setHostName(""));
    }

    /**
     * Test set NTP servers.
     * @throws Exception the exception
     */
    @Test
    void testSetNtpServers() throws Exception {
        final String[] previous = NetworkUtilities.getInstance().getNtpServers();
        final String[] expected = new String[] {
                "0.fr.pool.ntp.org"
        };

        NetworkUtilities.getInstance().setNtpServers(expected);

        final String[] results = NetworkUtilities.getInstance().getNtpServers();

        try {
            assertNotNull(results, "Results is null");
            assertTrue(results.length > 0, "Results is wrong");
            assertEquals(expected[0], results[0], "Results is wrong");
        } finally {
            NetworkUtilities.getInstance().setNtpServers(previous);
        }
    }

    /**
     * Test set NTP servers.
     * @throws Exception the exception
     */
    @Test
    void testSetNtpServersWithInvalid() throws Exception {
        final String[] previous = NetworkUtilities.getInstance().getNtpServers();
        final NetworkUtilities utils = NetworkUtilities.getInstance();

        try {
            utils.setNtpServers((String[]) null);
            utils.setNtpServers((String) null);
            utils.setNtpServers("");

            assumeTrue(true);
        } finally {
            NetworkUtilities.getInstance().setNtpServers(previous);
        }
    }
}
