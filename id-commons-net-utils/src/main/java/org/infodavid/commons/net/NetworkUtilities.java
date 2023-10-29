package org.infodavid.commons.net;

import static org.apache.commons.lang3.SystemUtils.IS_OS_FREE_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC_OSX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_NET_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_OPEN_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_UNIX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.apache.commons.lang3.SystemUtils.OS_NAME;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.infodavid.commons.net.udp.DiscoveryListener;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.slf4j.Logger;

/**
 * The Class NetworkUtilities.
 */
@SuppressWarnings("static-method")
public abstract class NetworkUtilities {

    /** The hostname pattern. */
    public static final Pattern HOSTNAME_PATTERN = Pattern.compile("(?!-)[A-Z\\d-]{1,63}(?<!-)$", Pattern.CASE_INSENSITIVE);

    /** The Constant IP_PATTERN. */
    public static final Pattern IP_PATTERN = Pattern.compile("^(?:\\d{1,3}\\.){3}\\d{1,3}$");

    /** The Constant NETMASK_PATTERN. */
    public static final Pattern NETMASK_PATTERN = Pattern.compile("^(254|252|248|240|224|192|128)\\.0\\.0\\.0|255\\.(254|252|248|240|224|192|128|0)\\.0\\.0|255\\.255\\.(254|252|248|240|224|192|128|0)\\.0|255\\.255\\.255\\.(254|252|248|240|224|192|128|0)$");

    /** The Constant SINGLETON. */
    private static NetworkUtilities singleton;

    /** The Constant IS_NOT_SUPPORTED. */
    protected static final String IS_NOT_SUPPORTED = " is not supported";

    /** The Constant METHOD_IS_NOT_SUPPORTED_ON. */
    protected static final String METHOD_IS_NOT_SUPPORTED_ON = "Method is not supported on ";

    /**
     * Gets the single instance.
     * @return single instance
     */
    public static synchronized NetworkUtilities getInstance() {
        if (singleton == null) {
            if (IS_OS_FREE_BSD || IS_OS_LINUX || IS_OS_MAC || IS_OS_MAC_OSX || IS_OS_NET_BSD || IS_OS_OPEN_BSD || IS_OS_UNIX) {
                singleton = new LinuxNetworkUtilities();
            } else if (IS_OS_WINDOWS) {
                singleton = new WindowsNetworkUtilities();
            } else {
                throw new UnsupportedOperationException(OS_NAME + IS_NOT_SUPPORTED);
            }
        }

        return singleton;
    }

    /**
     * Instantiates a new net utilities.
     */
    protected NetworkUtilities() {
        // noop
    }

    /**
     * Close quietly.
     * @param socket the socket
     */
    public void closeQuietly(final Closeable socket) {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (@SuppressWarnings("unused") final Exception e) { // NOSONAR Quietly
            // noop
        }
    }

    /**
     * Discover hosts.
     * @param listener the listener
     * @throws InterruptedException the interrupted exception
     */
    public abstract void discover(final DiscoveryListener listener) throws InterruptedException;

    /**
     * Find a free TCP port.
     * @return the integer associated to the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int findFreeTcpPort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);

            return socket.getLocalPort();
        }
    }

    /**
     * Find a free TCP port.
     * @param port the initial port
     * @return the integer associated to the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public int findFreeTcpPort(final int port) throws IOException {
        for (int i = port; i <= 65535; i++) {
            try (Socket socket = new Socket()) {
                socket.setSoTimeout(200);
                socket.setSoLinger(true, 0);
                socket.connect(new InetSocketAddress(i), 200);

                if (!socket.isConnected()) {
                    return i;
                }
            } catch (@SuppressWarnings("unused") final SocketTimeoutException e) { // NOSONAR Expected exception
                return i;
            }
        }

        return -1;
    }

    /**
     * Gets the computer name.
     * @return the computer name
     */
    public String getComputerName() {
        return getHostName();
    }

    /**
     * Gets the DNS servers.
     * @return the servers
     */
    public abstract String[] getDnsServers();

    /**
     * Gets the computer name.
     * @return the computer name
     */
    public String getHostName() {
        final Map<String, String> env = System.getenv();

        if (env.containsKey("COMPUTERNAME")) {
            return env.get("COMPUTERNAME");
        }

        if (env.containsKey("HOSTNAME")) {
            return env.get("HOSTNAME");
        }

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            getLogger().warn("Cannot read the hostname", e);

            return StringUtils.EMPTY;
        }
    }

    /**
     * Gets the MAC address.
     * @param iface the interface
     * @return the address
     * @throws SocketException the socket exception
     */
    @SuppressWarnings("boxing")
    public String getMacAddress(final NetworkInterface iface) throws SocketException {
        final byte[] mac = iface.getHardwareAddress();

        if (mac == null) {
            return null;
        }

        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {
            buffer.append(String.format("%02X%s", mac[i], i < mac.length - 1 ? "-" : ""));
        }

        return buffer.toString();
    }

    /**
     * Gets the MAC addresses.
     * @return the addresses
     */
    public Set<String> getMacAddresses() {
        final Map<String, InterfaceDescription> ifaces = getNetworkInterfaces();

        if (ifaces == null) {
            return Collections.emptySet();
        }

        final Set<String> results = new HashSet<>();
        ifaces.values().forEach(i -> results.add(i.getMacAddress()));

        return results;
    }

    /**
     * Gets the MAC addresses.
     * @return the MAC addresses
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Set<Pair<String,String>> getMacAddressesInfo() throws IOException { // NOSONAR
        final Map<String,InterfaceDescription> ifaces = getNetworkInterfaces();

        if (ifaces == null) {
            return Collections.emptySet();
        }

        final Set<Pair<String,String>> results = new HashSet<>();

        ifaces.values().forEach(i -> {
            if (StringUtils.isEmpty(i.getIpv4Address())) {
                results.add(new ImmutablePair<>(i.getMacAddress(), i.getName()));
            }
            else {
                results.add(new ImmutablePair<>(i.getMacAddress(), i.getName() + " (" + i.getIpv4Address() + ')'));
            }
        });

        return results;
    }

    /**
     * Gets the network interfaces.
     * @return the network interfaces
     */
    public abstract Map<String, InterfaceDescription> getNetworkInterfaces();

    /**
     * Gets the NTP servers.
     * @return the servers
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract String[] getNtpServers();

    /**
     * Checks if is same address.
     * @param left  the first address
     * @param right the second address
     * @return true, if addresses are for the same host
     */
    public boolean isSameHost(final String left, final String right) {
        try {
            if (StringUtils.equalsIgnoreCase(left, right)) {
                return true;
            }

            final boolean isLeftLoopbackAddress = InetAddress.getByName(left).isLoopbackAddress();
            final boolean isRightLoopbackAddress = InetAddress.getByName(right).isLoopbackAddress();

            if (isLeftLoopbackAddress && isRightLoopbackAddress) {
                return true;
            }

            if (isLeftLoopbackAddress || isRightLoopbackAddress) {
                String other;

                if (isLeftLoopbackAddress) {
                    other = right;
                } else {
                    other = left;
                }

                final Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();

                while (ifaces.hasMoreElements()) {
                    final Enumeration<InetAddress> addresses = ifaces.nextElement().getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        final InetAddress addr = addresses.nextElement();

                        if (addr.getHostAddress().equals(other) || addr.getHostName().equals(other)) {
                            return true;
                        }
                    }
                }
            }
        } catch (@SuppressWarnings("unused") final IOException e) {
            // noop
        }

        return false;
    }

    /**
     * Ping.
     * @param host the host
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void ping(final String host) throws IOException {
        final boolean reachable = InetAddress.getByName(host).isReachable(200);

        if (!reachable) {
            throw new IOException(host + " is not reachable");
        }
    }

    /**
     * Ping.
     * @param host the host
     * @param port the port
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void ping(final String host, final int port) throws IOException {
        boolean reachable = false;

        try (Socket socket = new Socket()) {
            socket.setSoTimeout(200);
            socket.setSoLinger(true, 0);
            socket.connect(new InetSocketAddress(host, port), 200);
            reachable = socket.isConnected();
        }

        if (!reachable) {
            throw new IOException(host + " is not reachable on port " + port);
        }
    }

    /**
     * Sets the DNS servers.
     * @param domain  the domain
     * @param servers the servers
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void setDnsServers(String domain, String... servers) throws IOException;

    /**
     * Sets the host name.
     * @param value the new host name
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void setHostName(String value) throws IOException;

    /**
     * Sets the network interface.
     * @param data the new network interface
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void setNetworkInterface(InterfaceDescription data) throws IOException;

    /**
     * Sets the NTP servers.
     * @param servers the servers
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void setNtpServers(String... servers) throws IOException;

    /**
     * Execute.
     * @param command the command
     * @return the status code
     */
    protected int run(final String[] command) {
        int code = -1;

        try {
            code = CommandRunnerFactory.getInstance().run(command);
        } catch (final Exception e) {
            getLogger().warn("An error occured while executing command: " + command, e); // NOSONAR Always written
        }

        return code;
    }

    /**
     * Execute.
     * @param output  the output buffer
     * @param error   the error buffer
     * @param command the command
     * @return the status code
     */
    protected int run(final StringBuilder output, final StringBuilder error, final String... command) {
        int code = -1;

        try {
            code = CommandRunnerFactory.getInstance().run(output, error, command);
        } catch (final Exception e) {
            getLogger().warn("An error occured while executing command: " + command, e); // NOSONAR Always written
        }

        return code;
    }

    /**
     * Gets the logger.
     * @return the logger
     */
    protected abstract Logger getLogger();
}
