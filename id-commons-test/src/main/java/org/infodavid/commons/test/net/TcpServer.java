package org.infodavid.commons.test.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TcpServer.
 */
public class TcpServer extends AbstractTcpComponent {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    /**
     * The main method.
     * @param args the arguments
     */
    @SuppressWarnings("resource")
    public static void main(final String[] args) {
        try (TcpServer s = new TcpServer(8080)) {
            s.open();
            final Scanner scanner = new Scanner(System.in);
            String line;

            do {
                System.out.println("Type the message and press enter to send it or press enter to stop the server......"); // NOSONAR For testing
                line = scanner.nextLine();

                if (!line.trim().isEmpty()) {
                    s.send(line);
                }
            } while (!line.trim().isEmpty());
        } catch (final Exception e) {
            e.printStackTrace();// NOSONAR For testing
        } finally {
            System.exit(0);
        }
    }

    /** The acceptor. */
    private final IoAcceptor acceptor = new NioSocketAcceptor();

    /**
     * Instantiates a new server.
     * @param port the port
     */
    public TcpServer(final int port) {
        super(port);
    }

    /*
     * (non-javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public synchronized void close() {
        final List<IoSession> sessions = new ArrayList<>(acceptor.getManagedSessions().values());

        for (final IoSession session : sessions) {
            session.closeNow();
            LOGGER.info("Session closed: {}", session.getRemoteAddress());
        }

        acceptor.unbind();
        acceptor.dispose(false);
        LOGGER.info("Socket closed.");
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.AbstractTcpComponent#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * Checks for client.
     * @return true, if successful
     */
    public boolean hasClient() {
        return acceptor.getManagedSessionCount() > 0;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.AbstractTcpComponent#isActive()
     */
    @Override
    public boolean isActive() {
        return acceptor.isActive();
    }

    /**
     * Open.
     * @return the server
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public synchronized TcpServer open() throws IOException {
        acceptor.setHandler(handler);
        acceptor.getFilterChain().addLast("logger", new LoggingFilter("TCPServer"));
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.bind(new InetSocketAddress(getPort()));

        return this;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.AbstractTcpComponent#send(byte[])
     */
    @Override
    public void send(final byte[] message) throws IOException {
        if (message == null) {
            LOGGER.warn("Cannot send a null message"); // NOSONAR
        }

        final String data = new String(message, getCharset());
        final IoBuffer buffer = IoBuffer.wrap(message);

        for (final IoSession session : acceptor.getManagedSessions().values()) {
            LOGGER.info("Writing {} to session: {}", data, session.getRemoteAddress());
            session.write(buffer);
        }
    }
}
