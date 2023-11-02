package org.infodavid.commons.net;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.infodavid.commons.net.udp.DiscoveryListener;
import org.infodavid.commons.system.CommandRunner;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WindowsNetworkUtilities.
 */
class WindowsNetworkUtilities extends NetworkUtilities {

    /** The Constant DNS_COMMAND. */
    private static final String[] DNS_COMMAND = {
            "wmic", "nicconfig", "where", "ipenabled=TRUE", "get", "dnsserversearchorder", "/format:csv"
    };

    /** The Constant GETMAC_COMMAND. */
    private static final String[] GETMAC_COMMAND = {
            "getmac", "/v", "/NH", "/FO", "csv"
    };

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsNetworkUtilities.class);

    /** The Constant NICCONFIG_COMMAND. */
    private static final String[] NICCONFIG_COMMAND = {
            "wmic", "nicconfig", "where", "macaddress!=null", "get", "ipaddress,ipsubnet,defaultipgateway,macaddress,ipenabled,dhcpenabled", "/format:csv"
    };

    /** The Constant PARSING_LINE. */
    private static final String PARSING_LINE = "Parsing line: {}";

    /**
     * Instantiates a new network utilities.
     */
    public WindowsNetworkUtilities() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#discover(org.infodavid.commons.net.udp.DiscoveryListener)
     */
    @Override
    public void discover(final DiscoveryListener listener) throws InterruptedException {
        new WindowsArpScanner().discover(listener);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getDnsServers()
     */
    @Override
    public String[] getDnsServers() {
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        final CommandRunner executor = CommandRunnerFactory.getInstance();
        LOGGER.info("Retrieving network DNS servers using command: {}", Arrays.toString(DNS_COMMAND)); // NOSONAR Always written

        if (executor.run(output, error, DNS_COMMAND) == 0) {
            boolean header = true;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            for (final String line : output.toString().split("\\r?\\n")) { // NOSONAR Keep continue statements
                if (StringUtils.isEmpty(StringUtils.trim(line)) || line.charAt(0) == '#') {
                    continue;
                }

                if (header) {
                    header = false;

                    continue;
                }

                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(PARSING_LINE, line);
                }

                // 0 hostname
                // 1 DNS servers
                final String[] cells = StringUtils.splitPreserveAllTokens(line, ',');

                return cells[1].trim().replace("{", "").replace("}", "").split(";");
            }
        } else {
            LOGGER.warn("Error in command execution: {}", error.toString()); // NOSONAR Always written
        }

        return new String[0];
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getNetworkInterfaces()
     */
    @Override
    public Map<String, InterfaceDescription> getNetworkInterfaces() {
        final Map<String, InterfaceDescription> results = new HashMap<>();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        final CommandRunner executor = CommandRunnerFactory.getInstance();
        LOGGER.info("Retrieving network interfaces using command: {}", Arrays.toString(GETMAC_COMMAND)); // NOSONAR Always written

        if (executor.run(output, error, GETMAC_COMMAND) == 0) {
            InterfaceDescription entry = null;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            for (final String line : output.toString().split("\\r?\\n")) {
                if (StringUtils.isEmpty(line) || line.charAt(0) == '#') {
                    continue;
                }

                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(PARSING_LINE, line);
                }

                // 0 interface name
                // 1 driver name
                // 2 mac address (with - delimiter)
                final String[] cells = line.split("\",\"");
                entry = new InterfaceDescription();
                entry.setName(cells[0].substring(1).trim());
                entry.setMacAddress(cells[2].trim().toUpperCase().replace('-', ':').replaceAll("_|\\s", "")); // NOSONAR Keep pattern as it is
                results.put(entry.getName(), entry);
            }

            output.setLength(0);
        } else {
            LOGGER.warn("Error in command execution: {}", error.toString()); // NOSONAR Always written
        }

        LOGGER.info("Retrieving network interfaces using command: {}", Arrays.toString(NICCONFIG_COMMAND)); // NOSONAR Always written

        if (executor.run(output, error, NICCONFIG_COMMAND) == 0) {
            InterfaceDescription entry;
            boolean header = true;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            for (final String line : output.toString().split("\\r?\\n")) { // NOSONAR Keep continue statements
                if (StringUtils.isEmpty(StringUtils.trim(line)) || line.charAt(0) == '#') {
                    continue;
                }

                if (header) {
                    header = false;

                    continue;
                }

                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(PARSING_LINE, line);
                }

                // 0 hostname
                // 1 gateway ipv4 address
                // 2 is dhcp enabled (TRUE or FALSE)
                // 3 ip v4 address
                // 4 is enabled (TRUE or FALSE)
                // 5 netmask (example: 255.255.255.0)
                // 6 mac address (with : delimiter)
                final String[] cells = line.split(",");

                if (cells.length == 7) {
                    entry = results.get(cells[6].trim().toUpperCase().replaceAll("-|_|\\s|:", "")); // NOSONAR Keep pattern as it is
                } else {
                    entry = null;
                }

                if (entry != null) {
                    String[] values = cells[1].trim().replace("{", "").replace("}", "").split(";");

                    if (values.length > 0) {
                        entry.setGateway(values[0]);
                    }

                    entry.setDhcp("TRUE".equalsIgnoreCase(cells[2].trim()));
                    values = cells[3].trim().replace("{", "").replace("}", "").split(";");

                    if (values.length > 0) {
                        entry.setIpv4Address(values[0]);
                    }

                    entry.setConnected("TRUE".equalsIgnoreCase(cells[4].trim()));
                    values = cells[5].trim().replace("{", "").replace("}", "").split(";");

                    if (values.length > 0) {
                        entry.setNetmask(values[0]);
                    }
                }
            }

            output.setLength(0);
        } else {
            LOGGER.warn("Error in command execution: {}", error.toString()); // NOSONAR Always written
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getNtpServers()
     */
    @Override
    public synchronized String[] getNtpServers() {
        throw new UnsupportedOperationException(METHOD_IS_NOT_SUPPORTED_ON + SystemUtils.OS_NAME);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setDnsServers(java.lang.String, java.lang.String[])
     */
    @Override
    public synchronized void setDnsServers(final String domain, final String... servers) throws IOException {
        throw new UnsupportedOperationException(METHOD_IS_NOT_SUPPORTED_ON + SystemUtils.OS_NAME);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setHostName(java.lang.String)
     */
    @Override
    public synchronized void setHostName(final String value) throws IOException {
        throw new UnsupportedOperationException(METHOD_IS_NOT_SUPPORTED_ON + SystemUtils.OS_NAME);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setNetworkInterface(org.infodavid.commons.net.InterfaceDescription)
     */
    @Override
    public synchronized void setNetworkInterface(final InterfaceDescription data) throws IOException {
        throw new UnsupportedOperationException(METHOD_IS_NOT_SUPPORTED_ON + SystemUtils.OS_NAME);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setNtpServers(java.lang.String[])
     */
    @Override
    public synchronized void setNtpServers(final String... servers) throws IOException {
        throw new UnsupportedOperationException(METHOD_IS_NOT_SUPPORTED_ON + SystemUtils.OS_NAME);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }
}
