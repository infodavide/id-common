package org.infodavid.commons.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DiscoveryServiceThread.
 */
class DiscoveryServiceThread extends Thread implements Thread.UncaughtExceptionHandler {

    /** The Constant DEFAULT_CHARSET. */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoveryServiceThread.class);

    /** The Constant DEFAULT_DISCOVERY_COMMAND. */
    protected static final byte[] DEFAULT_DISCOVERY_COMMAND;

    /** The Constant DEFAULT_DISCOVERY_PORT. */
    protected static final short DEFAULT_DISCOVERY_PORT = 9434;

    /** The default timeout. */
    protected static final short DEFAULT_TIMEOUT = 4000;

    static {
        DEFAULT_DISCOVERY_COMMAND = "AreYouThere".getBytes(StandardCharsets.UTF_8); // NOSONAR
    }

    /** The active. */
    private final AtomicBoolean active = new AtomicBoolean(true);

    /** The command. */
    private final byte[] command;

    /** The discovery port. */
    private final int discoveryPort;

    /** The response. */
    private final byte[] response;

    /** The timeout. */
    private final int timeout;

    /**
     * Instantiates a new discovery thread.
     * @param response the response
     */
    public DiscoveryServiceThread(final byte[] response) {
        setUncaughtExceptionHandler(this);
        discoveryPort = DEFAULT_DISCOVERY_PORT;
        command = DEFAULT_DISCOVERY_COMMAND;
        timeout = DEFAULT_TIMEOUT;
        this.response = response;
    }

    /**
     * Instantiates a new discovery thread.
     * @param command  the command
     * @param response the response
     */
    public DiscoveryServiceThread(final byte[] command, final byte[] response) {
        setUncaughtExceptionHandler(this);
        discoveryPort = DEFAULT_DISCOVERY_PORT;
        timeout = DEFAULT_TIMEOUT;
        this.command = command;
        this.response = response;
    }

    /**
     * Instantiates a new discovery thread.
     * @param command  the command
     * @param response the response
     * @param timeout  the timeout
     */
    public DiscoveryServiceThread(final byte[] command, final byte[] response, final int timeout) {
        setUncaughtExceptionHandler(this);
        discoveryPort = DEFAULT_DISCOVERY_PORT;
        this.command = command;
        this.response = response;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    /**
     * Instantiates a new discovery thread.
     * @param response the response
     * @param timeout  the timeout
     */
    public DiscoveryServiceThread(final byte[] response, final int timeout) {
        setUncaughtExceptionHandler(this);
        discoveryPort = DEFAULT_DISCOVERY_PORT;
        command = DEFAULT_DISCOVERY_COMMAND;
        this.response = response;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    /**
     * Instantiates a new discovery thread.
     * @param discoveryPort the discovery port
     * @param response      the response
     */
    public DiscoveryServiceThread(final int discoveryPort, final byte[] response) {
        setUncaughtExceptionHandler(this);
        command = DEFAULT_DISCOVERY_COMMAND;
        timeout = DEFAULT_TIMEOUT;
        this.discoveryPort = discoveryPort <= 0 ? DEFAULT_DISCOVERY_PORT : discoveryPort;
        this.response = response;
    }

    /**
     * Instantiates a new discovery thread.
     * @param discoveryPort the discovery port
     * @param command       the command
     * @param response      the response
     */
    public DiscoveryServiceThread(final int discoveryPort, final byte[] command, final byte[] response) {
        setUncaughtExceptionHandler(this);
        timeout = DEFAULT_TIMEOUT;
        this.discoveryPort = discoveryPort <= 0 ? DEFAULT_DISCOVERY_PORT : discoveryPort;
        this.command = command;
        this.response = response;
    }

    /**
     * Instantiates a new discovery thread.
     * @param discoveryPort the discovery port
     * @param command       the command
     * @param response      the response
     * @param timeout       the timeout
     */
    public DiscoveryServiceThread(final int discoveryPort, final byte[] command, final byte[] response, final int timeout) {
        setUncaughtExceptionHandler(this);
        this.discoveryPort = discoveryPort <= 0 ? DEFAULT_DISCOVERY_PORT : discoveryPort;
        this.command = command;
        this.response = response;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    /**
     * Instantiates a new discovery thread.
     * @param discoveryPort the discovery port
     * @param response      the response
     * @param timeout       the timeout
     */
    public DiscoveryServiceThread(final int discoveryPort, final byte[] response, final int timeout) {
        setUncaughtExceptionHandler(this);
        command = DEFAULT_DISCOVERY_COMMAND;
        this.discoveryPort = discoveryPort <= 0 ? DEFAULT_DISCOVERY_PORT : discoveryPort;
        this.response = response;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    /**
     * Checks if is active.
     * @return true, if is active
     */
    public boolean isActive() {
        return active.get();
    }

    /*
     * (non-javadoc)
     * @see java.lang.Thread#run()
     */
    /*
     * (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() { // NOSONAR No complexity
        final byte[] recvBuf = new byte[15000];
        byte[] receivedData;

        try (DatagramSocket socket = new DatagramSocket(discoveryPort, InetAddress.getByName("0.0.0.0"))) {
            socket.setBroadcast(true);
            socket.setSoTimeout(timeout);
            LOGGER.info("Ready to receive discovery broadcast packets on port: {}", String.valueOf(discoveryPort)); // NOSONAR Always written

            while (active.get()) {
                final DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

                try { // NOSONAR Catch timeout
                    socket.receive(packet);

                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Discovery packet received from: {}", packet.getAddress().getHostAddress());
                        LOGGER.trace("Data: {}", new String(packet.getData(), Charset.defaultCharset()));
                    }

                    receivedData = ArrayUtils.subarray(packet.getData(), packet.getOffset(), packet.getLength());

                    if (Objects.deepEquals(command, receivedData)) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("Sending response...");
                        }

                        final DatagramPacket sendPacket = new DatagramPacket(response, response.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);

                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("Packet sent to: {}", sendPacket.getAddress().getHostAddress());
                        }
                    } else if (receivedData != null && LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Unknown command: {}", new String(receivedData, StandardCharsets.UTF_8));
                    }
                } catch (@SuppressWarnings("unused") final SocketTimeoutException e) {
                    // noop
                }
            }
        } catch (final IOException e) {
            LOGGER.warn("An exception occurs while listening for discovery", e);
        } finally {
            LOGGER.trace("Stop listening");
        }
    }

    /**
     * Sets the active.
     * @param flag the new active
     */
    public void setActive(final boolean flag) {
        active.set(flag);
    }

    /*
     * (non-javadoc)
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable e) {
        LOGGER.error("An exception occurs while listening for discovery", e);
    }
}
