package org.infodavid.commons.net.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.infodavid.commons.concurrency.ThreadUtilities;
import org.infodavid.commons.net.HostDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DiscoveryClient.
 */
@SuppressWarnings("unused")
public class DiscoveryClient implements AutoCloseable, Closeable {

    /**
     * The Class DispatcherRunnable.
     */
    private static class DispatcherRunnable implements Runnable {

        /** The charset. */
        private final Charset charset;

        /** The listener. */
        private final DiscoveryListener listener;

        /** The packet. */
        private final DatagramPacket packet;

        /**
         * Instantiates a new dispatcher runnable.
         * @param packet   the packet
         * @param charset  the charset
         * @param listener the listener
         */
        public DispatcherRunnable(final DatagramPacket packet, final Charset charset, final DiscoveryListener listener) {
            this.packet = packet;
            this.charset = charset;
            this.listener = listener;
        }

        /*
         * (non-javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            LOGGER.debug("Processing response from host: {}", packet.getAddress());

            try {
                final HostDescription info = HostDescription.parse(new String(ArrayUtils.subarray(packet.getData(), packet.getOffset(), packet.getLength()), charset));

                if (info == null) {
                    LOGGER.warn("Invalid response from host: {}", packet.getAddress());
                } else {
                    listener.received(info, packet.getAddress());
                }
            } catch (final Exception e) {
                LOGGER.error("Malformed response from host: {}", packet.getAddress(), e);
            }
        }
    }

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryClient.class);

    /** The active. */
    private final AtomicBoolean active = new AtomicBoolean(true);

    /** The broadcast address. */
    private InetAddress broadcastAddress;

    /** The charset. */
    private Charset charset = DiscoveryServiceThread.DEFAULT_CHARSET;

    /** The command. */
    private byte[] command = DiscoveryServiceThread.DEFAULT_DISCOVERY_COMMAND;

    /** The discovery port. */
    private int discoveryPort = DiscoveryServiceThread.DEFAULT_DISCOVERY_PORT;

    /** The executor. */
    private final ScheduledExecutorService executor = ThreadUtilities.getInstance().newScheduledExecutorService(getClass(), LOGGER, 4);

    /** The stop feature. */
    @SuppressWarnings("rawtypes")
    private ScheduledFuture stopFeature = null;

    /** The timeout. */
    private int timeout = DiscoveryServiceThread.DEFAULT_TIMEOUT;

    /**
     * Instantiates a new discovery client.
     */
    public DiscoveryClient() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws IOException {
        active.set(false);

        try {
            executor.shutdownNow();
        } catch (final Exception e) {
            LOGGER.trace("Shutdown error", e);
        }
    }

    /**
     * Discover the hosts in the network.
     * @param listener the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void discover(final DiscoveryListener listener) throws IOException { // NOSONAR No complexity
        LOGGER.info("Starting discovery of hosts using UDP port: {}", String.valueOf(discoveryPort)); // NOSONAR Always written
        active.set(true);
        executor.submit(() -> {
            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setBroadcast(true);
                socket.setSoTimeout(timeout);
                List<InetAddress> broadcastAddresses;

                if (broadcastAddress == null) {
                    broadcastAddresses = new ArrayList<>();
                    final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

                    while (interfaces.hasMoreElements()) {
                        final NetworkInterface networkInterface = interfaces.nextElement();

                        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                            continue; // Don't want to broadcast to the loopback interface
                        }

                        for (final InterfaceAddress iaddr : networkInterface.getInterfaceAddresses()) {
                            final InetAddress addr = iaddr.getBroadcast();

                            if (addr == null) {
                                continue;
                            }

                            broadcastAddresses.add(addr);
                        }
                    }
                } else {
                    broadcastAddresses = Collections.singletonList(broadcastAddress);
                }

                for (final InetAddress addr : broadcastAddresses) {
                    socket.send(new DatagramPacket(command, command.length, addr, discoveryPort));

                    LOGGER.info("Request packet using address: {}", addr.getHostAddress());
                }

                while (active.get()) {
                    final byte[] buffer = new byte[15000];
                    final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    try {
                        socket.receive(packet);
                        LOGGER.debug("Response from host: {}", packet.getAddress());
                        executor.submit(new DispatcherRunnable(packet, charset, listener));
                    } catch (@SuppressWarnings("unused") final SocketTimeoutException e) {
                        // noop
                    }
                }

                if (stopFeature != null) {
                    stopFeature.cancel(false);
                }
            } catch (final IOException e) {
                LOGGER.error("Discovery failure", e);
            } finally {
                active.set(false);

                if (stopFeature != null) {
                    stopFeature.cancel(false);
                }

                LOGGER.info("Discovery terminated");
                listener.stopped();
            }
        });

        stopFeature = executor.schedule(() -> IOUtils.closeQuietly(DiscoveryClient.this), 60, TimeUnit.SECONDS);
    }

    /**
     * Gets the broadcast address.
     * @return the broadcastAddress
     */
    public InetAddress getBroadcastAddress() {
        return broadcastAddress;
    }

    /**
     * Gets the charset.
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Gets the command.
     * @return the command
     */
    public byte[] getCommand() {
        return command;
    }

    /**
     * Gets the discovery port.
     * @return the discoveryPort
     */
    public int getDiscoveryPort() {
        return discoveryPort;
    }

    /**
     * Gets the timeout.
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Checks if is active.
     * @return true, if is active
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * Sets the broadcast address.
     * @param broadcastAddress the broadcastAddress to set
     */
    public void setBroadcastAddress(final InetAddress broadcastAddress) {
        this.broadcastAddress = broadcastAddress;
    }

    /**
     * Sets the charset.
     * @param charset the charset to set
     */
    public void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Sets the command.
     * @param command the command to set
     */
    public void setCommand(final byte[] command) {
        this.command = command;

        if (command == null || command.length == 0) {
            this.command = DiscoveryServiceThread.DEFAULT_DISCOVERY_COMMAND;
            LOGGER.warn("Invalid discovery command, using: {}", String.valueOf(this.command)); // NOSONAR Always written
        }
    }

    /**
     * Sets the discovery port.
     * @param discoveryPort the discoveryPort to set
     */
    public void setDiscoveryPort(final int discoveryPort) {
        this.discoveryPort = discoveryPort;

        if (discoveryPort <= 1000) {
            this.discoveryPort = DiscoveryServiceThread.DEFAULT_DISCOVERY_PORT;
            LOGGER.warn("Invalid discovery port, using: {}", String.valueOf(this.discoveryPort)); // NOSONAR Always written
        }
    }

    /**
     * Sets the timeout.
     * @param timeout the timeout to set
     */
    public void setTimeout(final int timeout) {
        this.timeout = timeout;

        if (timeout <= 0) {
            this.timeout = DiscoveryServiceThread.DEFAULT_TIMEOUT;
            LOGGER.warn("Invalid discovery timeout, using: {}", String.valueOf(this.timeout)); // NOSONAR Always written
        }
    }
}
