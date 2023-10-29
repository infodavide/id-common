package org.infodavid.commons.net;

import static org.apache.commons.lang3.ArrayUtils.add;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.io.PathUtilities;
import org.infodavid.commons.net.udp.DiscoveryListener;
import org.infodavid.commons.system.CommandRunner;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LinuxNetworkUtilities.</br>
 * Caution: Some methods used to configure the network require a sudo rule.</br>
 * The shell script are copied from .jar resources to a temporary and hidden file on the user home directory.</br>
 * Currently, the following files are copied to the home directory and require a sudo rule:
 * <ul>
 * <li>.set_dns.sh</li>
 * <li>.set_interface.sh</li>
 * <li>.set_ntp.sh</li>
 * </ul>
 */
class LinuxNetworkUtilities extends NetworkUtilities {

    /** The Constant BIN_SUDO. Note: Full path of sudo command is not always the same. */
    private static final String BIN_SUDO = "sudo";

    /** The Constant ERROR_IN_COMMAND_EXECUTION. */
    private static final String ERROR_IN_COMMAND_EXECUTION = "Error in command execution: %s";

    /** The Constant ERROR_IN_FILE_READING. */
    private static final String ERROR_IN_FILE_READING = "Error when reading file: %s";

    /** The Constant HOSTNAME_COMMAND. */
    private static final String[] HOSTNAME_COMMAND = {
            "hostname"
    };

    /** The Constant HOSTNAMECTL_SET_HOSTNAME. Note: Full path of sudo command is not always the same. */
    private static final String[] HOSTNAMECTL_SET_HOSTNAME = {
            "sudo", "hostnamectl", "set-hostname"
    };

    /** The Constant IFCONFIG_FILE. */
    private static final String IFCONFIG_FILE = "ifconfig.sh";

    /** The Constant IP_FILE. */
    private static final String IP_FILE = "ip.sh";

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxNetworkUtilities.class);

    /** The Constant NAMESERVER_PATTERN. */
    private static final Pattern NAMESERVER_PATTERN = Pattern.compile("^nameserver\\s+([\\d\\.]+)$", Pattern.CASE_INSENSITIVE);

    /** The Constant QUOTE. */
    private static final String QUOTE = "'";

    /** The Constant RESOLV_CONF_FILE. */
    private static final String RESOLV_CONF_FILE = "/etc/resolv.conf";

    /** The Constant SET_DNS_FILE. */
    private static final String SET_DNS_FILE = "set_dns.sh";

    /** The Constant SET_INTERFACE_FILE. */
    private static final String SET_INTERFACE_FILE = "set_interface.sh";

    /** The Constant SET_NTP_FILE. */
    private static final String SET_NTP_FILE = "set_ntp.sh";

    /** The Constant TIMESYNCD_CONF_FILE. */
    private static final String TIMESYNCD_CONF_FILE = "/etc/systemd/timesyncd.conf";

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#discover(org.infodavid.commons.net.udp.DiscoveryListener)
     */
    @Override
    public void discover(final DiscoveryListener listener) throws InterruptedException {
        new LinuxArpScanner().discover(listener);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getDnsServers()
     */
    @Override
    public synchronized String[] getDnsServers() {
        final Path file = Paths.get(RESOLV_CONF_FILE);

        if (!Files.exists(file)) {
            return new String[0];
        }

        final Set<String> results = new LinkedHashSet<>();

        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                final Matcher matcher = NAMESERVER_PATTERN.matcher(line);
                if (matcher.matches()) {
                    results.add(matcher.group(1));
                }
            }
        } catch (final IOException e) {
            LOGGER.warn(String.format(ERROR_IN_FILE_READING, RESOLV_CONF_FILE), e);
        }

        // Remove duplicated
        return new LinkedHashSet<>(results).toArray(new String[results.size()]);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getHostName()
     */
    @Override
    public String getHostName() { // NOSONAR No synchronization with the setter
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();

        if (run(output, error, HOSTNAME_COMMAND) == 0) {
            for (final String line : output.toString().split("\n")) {
                if (!line.isEmpty()) {
                    return line;
                }
            }
        }

        return super.getHostName();
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * Copy script.
     * @param in     the in
     * @param script the script
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void copyScript(final InputStream in, final Path script) throws IOException {
        final PathUtilities utils = PathUtilities.getInstance();
        utils.copy(in, script);
        utils.setExecutable(script);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getNetworkInterfaces()
     */
    @Override
    public synchronized Map<String, InterfaceDescription> getNetworkInterfaces() {
        final Map<String, InterfaceDescription> results = new HashMap<>();
        final PathUtilities utils = PathUtilities.getInstance();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        // We use a temporary hidden script without random name to format information
        final Path script = utils.getUserPath().resolve('.' + IP_FILE);
        final CommandRunner executor = CommandRunnerFactory.getInstance();

        try {
            LOGGER.debug("Trying to retrieve network interfaces using a shell script based on ip command");
            Exception exception = null;
            int exitCode = 0;

            try (InputStream in = getClass().getResourceAsStream(IP_FILE)) {
                copyScript(in, script);
                exitCode = executor.run(output, error, new String[] {
                        script.toAbsolutePath().toString()
                });

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(output.toString());
                }

                if (exitCode != 0) {
                    LOGGER.warn(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString())); // NOSONAR Always written
                    output.setLength(0);
                    error.setLength(0);
                }
            } catch (final Exception e) {
                exception = e;
                exitCode = -1;
            }

            if (exitCode != 0) {
                LOGGER.debug("Trying to retrieve network interfaces using a shell script based on ifconfig command");

                try (InputStream in = getClass().getResourceAsStream(IFCONFIG_FILE)) {
                    copyScript(in, script);
                    exitCode = executor.run(output, error, new String[] {
                            script.toAbsolutePath().toString()
                    });

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(output.toString());
                    }

                    if (exitCode != 0) {
                        LOGGER.warn(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString())); // NOSONAR Always written
                        output.setLength(0);
                        error.setLength(0);
                    }
                } catch (final Exception e) {
                    exception = e;
                    exitCode = -1;
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Output:\n{}", output);
            }

            if (exitCode == 0) {
                for (final String line : output.toString().split("\\r?\\n")) {
                    if (StringUtils.isEmpty(StringUtils.trim(line)) || line.charAt(0) == '#') {
                        continue;
                    }

                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Parsing line: {}", line);
                    }

                    // 0 interface name
                    // 1 ip v4 address
                    // 2 netmask (example: 255.255.255.0)
                    // 3 gateway ipv4 address
                    // 4 mac address (with : delimiter)
                    // 5 state (UP or else)
                    // 6 dhcp (ENABLED or else)
                    final String[] cells = StringUtils.splitPreserveAllTokens(line, ',');

                    if (cells.length > 5) {
                        final InterfaceDescription entry = new InterfaceDescription();
                        entry.setName(cells[0].trim());
                        entry.setIpv4Address(cells[1].trim());
                        entry.setNetmask(cells[2].trim());
                        entry.setGateway(cells[3].trim());
                        entry.setMacAddress(cells[4].trim().toUpperCase().replaceAll("-|_|\\s", "")); // NOSONAR Keep pattern as it is
                        entry.setConnected("UP".equalsIgnoreCase(cells[5].trim()));
                        entry.setDhcp("ENABLED".equalsIgnoreCase(cells[6].trim()));
                        entry.setEnabled(entry.isConnected() || StringUtils.isNotEmpty(entry.getIpv4Address()));
                        results.put(entry.getName(), entry);
                    }
                }
            } else if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()), exception);
            } else {
                LOGGER.debug(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()));
            }
        } finally {
            utils.deleteQuietly(script);
        }

        return results;
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#getNtpServers()
     */
    @Override
    public synchronized String[] getNtpServers() {
        final Path file = Paths.get(TIMESYNCD_CONF_FILE);

        if (!Files.exists(file)) {
            return new String[0];
        }

        final List<String> results = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(Files.newBufferedReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (StringUtils.startsWithIgnoreCase(line, "FallbackNTP=")) {
                    for (final String item : StringUtils.substringAfter(line, "=").split("\\s+")) {
                        results.add(item.trim());
                    }
                }

                if (StringUtils.startsWithIgnoreCase(line, "NTP=")) {
                    for (final String item : StringUtils.substringAfter(line, "=").split("\\s+")) {
                        results.add(0, item.trim());
                    }
                }
            }
        } catch (final IOException e) {
            LOGGER.warn(String.format(ERROR_IN_FILE_READING, RESOLV_CONF_FILE), e);
        }

        // Remove duplicated
        return new LinkedHashSet<>(results).toArray(new String[results.size()]);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setDnsServers(java.lang.String, java.lang.String[])
     */
    @Override
    public synchronized void setDnsServers(final String domain, final String... servers) throws IOException {
        if (servers == null || servers.length == 0) {
            throw new IllegalArgumentException("At least one DNS server is required");
        }

        final LinkedHashSet<String> addresses = new LinkedHashSet<>();

        for (final String item : servers) {
            if (StringUtils.isNotEmpty(item) && IP_PATTERN.matcher(item).matches()) {
                addresses.add(item);
            }
        }

        if (addresses.isEmpty()) {
            throw new IllegalArgumentException("At least one valid DNS server is required");
        }

        final PathUtilities utils = PathUtilities.getInstance();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        // We use a temporary hidden script without random name to allow sudo rule
        final Path script = utils.getUserPath().resolve('.' + SET_DNS_FILE);
        final CommandRunner executor = CommandRunnerFactory.getInstance();

        try (InputStream in = getClass().getResourceAsStream(SET_DNS_FILE)) {
            LOGGER.info("Updating DNS servers: {}", addresses);
            copyScript(in, script);
            String[] command = {
                    BIN_SUDO, script.toAbsolutePath().toString()
            };

            if (StringUtils.isEmpty(domain)) {
                command = ArrayUtils.add(command, "-d");
                command = ArrayUtils.add(command, "intranet");
            } else {
                command = ArrayUtils.add(command, "-d");
                command = ArrayUtils.add(command, domain);
            }

            for (final String address : addresses) {
                command = ArrayUtils.add(command, "-s");
                command = ArrayUtils.add(command, address);
            }

            final int exitCode = executor.run(output, error, command);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            if (exitCode != 0) {
                throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()));
            }
        } catch (final Exception e) {
            throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()), e);
        } finally {
            utils.deleteQuietly(script);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setHostName(java.lang.String)
     */
    @Override
    public synchronized void setHostName(final String value) throws IOException { // NOSONAR No synchronization with the getter
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("No value specified");
        }

        LOGGER.info("Updating hostname: {}", value);

        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        final int exitCode = CommandRunnerFactory.getInstance().run(output, error, add(HOSTNAMECTL_SET_HOSTNAME, QUOTE + value + QUOTE));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(output.toString());
        }

        if (exitCode != 0) {
            throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()));
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setNetworkInterface(org.infodavid.commons.net.InterfaceDescription)
     */
    @Override
    public synchronized void setNetworkInterface(final InterfaceDescription iface) throws IOException {
        if (iface == null) {
            throw new IllegalArgumentException("No data specified");
        }

        if (StringUtils.isEmpty(iface.getName())) {
            throw new IllegalArgumentException("No interface name specified");
        }

        if (!iface.isDhcp()) {
            if (StringUtils.isEmpty(iface.getIpv4Address()) || !IP_PATTERN.matcher(iface.getIpv4Address()).matches()) {
                throw new IllegalArgumentException("A valid IP address is required");
            }

            if (StringUtils.isNotEmpty(iface.getNetmask()) && !IP_PATTERN.matcher(iface.getNetmask()).matches()) {
                throw new IllegalArgumentException("Invalid netmask");
            }

            if (StringUtils.isNotEmpty(iface.getGateway()) && !IP_PATTERN.matcher(iface.getGateway()).matches()) {
                throw new IllegalArgumentException("Invalid gateway");
            }
        }

        final PathUtilities utils = PathUtilities.getInstance();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        // We use a temporary hidden script without random name to allow sudo rule
        final Path script = utils.getUserPath().resolve('.' + SET_INTERFACE_FILE);
        final CommandRunner executor = CommandRunnerFactory.getInstance();

        try (InputStream in = getClass().getResourceAsStream(SET_INTERFACE_FILE)) {
            LOGGER.info("Updating interface: {}", iface);
            copyScript(in, script);
            String[] command = {
                    BIN_SUDO, script.toAbsolutePath().toString(), "-i", iface.getName()
            };

            if (iface.isDhcp()) {
                command = ArrayUtils.add(command, "-t");
                command = ArrayUtils.add(command, "DHCP");
            } else {
                command = ArrayUtils.add(command, "-t");
                command = ArrayUtils.add(command, "STATIC");
                command = ArrayUtils.add(command, "-a");
                command = ArrayUtils.add(command, iface.getIpv4Address());

                if (StringUtils.isNotEmpty(iface.getNetmask())) {
                    command = ArrayUtils.add(command, "-m");
                    command = ArrayUtils.add(command, iface.getNetmask());
                }

                if (StringUtils.isNotEmpty(iface.getGateway())) {
                    command = ArrayUtils.add(command, "-g");
                    command = ArrayUtils.add(command, iface.getGateway());
                }
            }

            final int exitCode = executor.run(output, error, command);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            if (exitCode != 0) {
                throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()));
            }
        } catch (final Exception e) {
            throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()), e);
        } finally {
            utils.deleteQuietly(script);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.NetworkUtilities#setNtpServers(java.lang.String[])
     */
    @Override
    public synchronized void setNtpServers(final String... servers) throws IOException {
        final LinkedHashSet<String> addresses = new LinkedHashSet<>();

        if (servers != null && servers.length > 0) {
            for (final String item : servers) {
                if (StringUtils.isNotEmpty(item)) {
                    addresses.add(item);
                }
            }
        }

        final PathUtilities utils = PathUtilities.getInstance();
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();
        // We use a temporary hidden script without random name to allow sudo rule
        final Path script = utils.getUserPath().resolve('.' + SET_NTP_FILE);
        final CommandRunner executor = CommandRunnerFactory.getInstance();

        try (InputStream in = getClass().getResourceAsStream(SET_NTP_FILE)) {
            LOGGER.info("Updating NTP servers: {}", addresses);
            copyScript(in, script);
            String[] command = {
                    BIN_SUDO, script.toAbsolutePath().toString()
            };

            for (final String address : addresses) {
                command = ArrayUtils.add(command, "-s");
                command = ArrayUtils.add(command, address);
            }

            final int exitCode = executor.run(output, error, command);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(output.toString());
            }

            if (exitCode != 0) {
                throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()));
            }
        } catch (final Exception e) {
            throw new IOException(String.format(ERROR_IN_COMMAND_EXECUTION, error.toString()), e);

        } finally {
            utils.deleteQuietly(script);
        }
    }
}
