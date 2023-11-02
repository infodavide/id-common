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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

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
        final int port = NetworkUtilities.getInstance().findFreeTcpPort();

        System.out.println("Found free TCP port: " + port); //NOSONAR For testing
        assertTrue(port > 0, "Wrong TCP port");
    }

    /**
     * Test get computer name.
     * @throws Exception the exception
     */
    @Test
    void testGetComputerName() throws Exception {
        final String name = NetworkUtilities.getInstance().getComputerName();

        System.out.println("Compuer name: " + name); //NOSONAR For testing
        assertTrue(StringUtils.isNotEmpty(name), "Wrong computer name");
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
            System.out.println("Found MAC address: " + item); //NOSONAR For testing
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
            System.out.println("Found MAC address: " + item); //NOSONAR For testing
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

        for (final Entry<String, InterfaceDescription> entry : results.entrySet()) {
            System.out.println("Found interface: " + entry.getValue()); //NOSONAR For testing
            assertTrue(StringUtils.isNotEmpty(entry.getValue().getMacAddress()), "Wrong MAC address: " + entry.getValue().getMacAddress());
            assertTrue(StringUtils.isNotEmpty(entry.getValue().getName()), "Wrong interface description: " + entry.getValue().getName());
        }
    }

    /**
     * Test get DNS servers.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testGetDnsServers() throws Exception {
        final String[] results = NetworkUtilities.getInstance().getDnsServers();

        assertNotNull(results, "Results is null");

        for (final String item : results) {
            System.out.println("Found DNS server: " + item); //NOSONAR For testing
            assertTrue(StringUtils.isNotEmpty(item), "Wrong server address or name");
        }
    }

    /**
     * Test get NTP servers.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testGetNtpServers() throws Exception {
        final String[] results = NetworkUtilities.getInstance().getNtpServers();

        assertNotNull(results, "Results is null");

        for (final String item : results) {
            System.out.println("Found NTP server: " + item); //NOSONAR For testing
            assertTrue(StringUtils.isNotEmpty(item), "Wrong server address or name");
        }
    }
}
