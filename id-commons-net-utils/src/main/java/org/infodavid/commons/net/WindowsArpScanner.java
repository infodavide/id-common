package org.infodavid.commons.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;
import org.infodavid.commons.concurrency.ThreadUtilities;
import org.infodavid.commons.net.udp.DiscoveryListener;
import org.infodavid.commons.system.CommandRunner;
import org.infodavid.commons.system.CommandRunnerFactory;
import org.infodavid.commons.utility.SleepLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WindowsArpScanner.
 */
class WindowsArpScanner implements ArpScanner {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsArpScanner.class);

    /** The sleep lock. */
    private final SleepLock sleepLock = new SleepLock();

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.ArpScanner#discover(org.infodavid.commons.net.udp.DiscoveryListener)
     */
    @Override
    public void discover(final DiscoveryListener listener) throws InterruptedException {
        LOGGER.info("Starting ARP scan");
        final ExecutorService executor = ThreadUtilities.getInstance().newThreadPoolExecutor(getClass(), LOGGER, 10, 50);

        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                discover(networkInterface, executor, listener);
            }
        } catch (final Exception e) { // NOSONAR Exception logged
            LOGGER.error("ARP scan failure", e);
        } finally {
            ThreadUtilities.getInstance().shutdown(executor);
            LOGGER.info("ARP scan terminated");
            listener.stopped();
        }
    }

    /**
     * Discover.
     * @param networkInterface the network interface
     * @param executor         the executor
     * @param listener         the listener
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    private synchronized void discover(final NetworkInterface networkInterface, final ExecutorService executor, final DiscoveryListener listener) throws IOException, InterruptedException {
        final CommandRunner commandExecutor = CommandRunnerFactory.getInstance();
        final Set<String> addresses = new HashSet<>();
        sleepLock.lock();

        try {
            for (final InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                if (interfaceAddress.getAddress().isLoopbackAddress()) {
                    continue;
                }

                final SubnetUtils subnet = new SubnetUtils(interfaceAddress.getAddress().getHostAddress() + '/' + interfaceAddress.getNetworkPrefixLength());
                String[] stack = new String[] {
                        "cmd", "/c"
                };

                for (final String addr : subnet.getInfo().getAllAddresses()) {
                    stack = ArrayUtils.addAll(stack, "ping", addr, "-n", "1", "-w", "5", "&");
                    addresses.add(addr);

                    if (stack.length > 14) {
                        final String[] commands = ArrayUtils.addAll(stack, "echo", "1");
                        executor.submit(() -> Integer.valueOf(commandExecutor.run(commands)));
                        stack = new String[] {
                                "cmd", "/c"
                        };
                    }
                }

                if (stack.length > 2) {
                    final String[] commands = ArrayUtils.addAll(stack, "echo", "1");
                    executor.submit(() -> Integer.valueOf(commandExecutor.run(commands)));
                }
            }

            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            sleepLock.await(200);
            final StringBuilder output = new StringBuilder();
            final StringBuilder error = new StringBuilder();
            final int exitCode = commandExecutor.run(output, error, new String[] {
                    "cmd", "/c", "arp", "-a"
            });

            if (exitCode == 0) {
                for (final String line : output.toString().split("\\r?\\n")) { // NOSONAR Number of continue
                    if (StringUtils.isEmpty(StringUtils.trim(line)) || line.charAt(0) == '#') {
                        continue;
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Parsing line: {}", line);
                    }

                    final String[] parts = StringUtils.splitByWholeSeparator(line.trim(), null);

                    if (parts == null || parts.length == 0 || !Character.isDigit(parts[0].charAt(0)) || !addresses.contains(parts[0])) {
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
                    listener.received(result, addr);
                }
            }
        } finally {
            sleepLock.unlock();
        }
    }
}
