package org.infodavid.commons.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.test.TestCase;
import org.infodavid.commons.test.rules.ConditionalIgnore;
import org.infodavid.commons.test.rules.RunOnlyOnLinuxCondition;
import org.junit.jupiter.api.Test;

/**
 * The Class NetworkUtilitiesTest.
 */
@SuppressWarnings("static-method")
class NetworkUtilitiesTest extends TestCase {

    /**
     * Test find free TCP port.
     * @throws Exception the exception
     */
    @Test
    void testFindFreeTcpPort() throws Exception {
        assertTrue(NetworkUtilities.getInstance().findFreeTcpPort() > 0, "Wrong TCP port");
    }

    /**
     * Test get computer name.
     * @throws Exception the exception
     */
    @Test
    void testGetComputerName() throws Exception {
        assertTrue(StringUtils.isNotEmpty(NetworkUtilities.getInstance().getComputerName()), "Wrong computer name");
    }

    /**
     * Test get MAC addresses.
     * @throws Exception the exception
     */
    @Test
    void testGetMacAddresses() throws Exception {
        final Set<String> results = NetworkUtilities.getInstance().getMacAddresses();

        assertNotNull(results, "Results is null");
        assertTrue(results.size() > 0, "Wrong results");

        for (final String item : results) {
            assertTrue(StringUtils.isNotEmpty(item), "Wrong MAC address: " + item);

            if ("N/A".equals(item)) {
                continue;
            }

            assertNotEquals(-1, item.indexOf(':'), "MAC address contains ':'");
            assertEquals(-1, item.indexOf(' '), "MAC address contains a space");
            assertEquals(-1, item.indexOf('-'), "MAC address contains '-'");
            assertEquals(-1, item.indexOf('_'), "MAC address contains '_'");
        }
    }

    /**
     * Test get MAC addresses info.
     * @throws Exception the exception
     */
    @Test
    void testGetMacAddressesInfo() throws Exception {
        final Set<String> results = NetworkUtilities.getInstance().getMacAddresses();

        assertNotNull(results, "Results is null");
        assertTrue(results.size() > 0, "Wrong results");

        for (final String item : results) {
            assertTrue(StringUtils.isNotEmpty(item), "Wrong MAC address: " + item);
        }
    }

    /**
     * Test get MAC addresses info.
     * @throws Exception the exception
     */
    @Test
    void testGetNetworkInterfaces() throws Exception {
        final Map<String, InterfaceDescription> results = NetworkUtilities.getInstance().getNetworkInterfaces();

        assertNotNull(results, "Results is null");
        assertTrue(results.size() > 0, "Wrong results");

        for (final Entry<String, InterfaceDescription> item : results.entrySet()) {
            assertTrue(StringUtils.isNotEmpty(item.getValue().getMacAddress()), "Wrong MAC address: " + item.getValue().getMacAddress());
            assertTrue(StringUtils.isNotEmpty(item.getValue().getName()), "Wrong interface description: " + item.getValue().getName());
        }
    }

    /**
     * Test get DNS servers.
     * @throws Exception the exception
     */
    @ConditionalIgnore(condition = RunOnlyOnLinuxCondition.class)
    @Test
    void testGetDnsServers() throws Exception {
        final String[] results = NetworkUtilities.getInstance().getDnsServers();

        assertNotNull(results, "Results is null");

        for (final String item : results) {
            System.out.println(item);
            assertTrue(StringUtils.isNotEmpty(item), "Wrong server address or name");
        }
    }

    /**
     * Test get NTP servers.
     * @throws Exception the exception
     */
    @ConditionalIgnore(condition = RunOnlyOnLinuxCondition.class)
    @Test
    void testGetNtpServers() throws Exception {
        final String[] results = NetworkUtilities.getInstance().getNtpServers();

        assertNotNull(results, "Results is null");

        for (final String item : results) {
            System.out.println(item);
            assertTrue(StringUtils.isNotEmpty(item), "Wrong server address or name");
        }
    }
}
