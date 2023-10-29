package org.infodavid.commons.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.net.udp.DiscoveryListener;
import org.infodavid.commons.system.CommandRunner;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LinuxArpScanner.
 */
class LinuxArpScanner implements ArpScanner {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxArpScanner.class);

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.ArpScanner#discover(org.infodavid.commons.net.udp.DiscoveryListener)
     */
    @Override
    public void discover(final DiscoveryListener listener) {
        LOGGER.info("Starting ARP scan");

        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                discover(networkInterface, listener);
            }
        } catch (final IOException e) {
            LOGGER.error("ARP scan failure", e);
        } finally {
            LOGGER.info("ARP scan terminated");

            listener.stopped();
        }
    }

    /**
     * Discover.
     * @param networkInterface the network interface
     * @param listener         the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static synchronized void discover(final NetworkInterface networkInterface, final DiscoveryListener listener) throws IOException {
        final CommandRunner executor = CommandRunnerFactory.getInstance();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        int exitCode = 0;
        exitCode = executor.run(output, error, new String[] {
                "sudo", "arp-scan", "--interface=" + networkInterface.getName(), "--localnet", "-x"
        });

        if (exitCode == 0) {
            for (final String line : output.toString().split("\\r?\\n")) { // NOSONAR Number of continue
                if (StringUtils.isEmpty(StringUtils.trim(line)) || line.charAt(0) == '#') {
                    continue;
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Parsing line: {}", line);
                }

                final String[] parts = StringUtils.splitByWholeSeparator(line, null);

                if (parts == null || parts.length == 0) {
                    continue;
                }

                final HostDescription result = new HostDescription();
                final InterfaceDescription interfaceInfo = new InterfaceDescription();
                final InetAddress addr = InetAddress.getByName(parts[0]);
                interfaceInfo.setIpv4Address(parts[0]);
                interfaceInfo.setMacAddress(parts[1]);
                result.setName(addr.getCanonicalHostName());
                result.setInterfaces(new InterfaceDescription[] {
                        interfaceInfo
                });

                if (parts.length > 2) {
                    result.setSystemInformation(parts[2]);
                }

                listener.received(result, addr);
            }
        }
    }
}
