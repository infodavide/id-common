package org.infodavid.commons.test.net;

import java.io.IOException;

/**
 * The Class TcpListenerAdapter.
 */
public class TcpListenerAdapter implements TcpListener {

    /**
     * Instantiates a new adapter.
     */
    public TcpListenerAdapter() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.TcpListener#handleException(java.lang.Throwable)
     */
    @Override
    public void handleException(final Throwable e) {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.TcpListener#onClose(java.lang.Object)
     */
    @Override
    public void onClose(final Object source) {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.TcpListener#onData(java.lang.Object, byte[])
     */
    @Override
    public boolean onData(final Object source, final byte[] message) throws IOException {
        return false;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.TcpListener#onOpen(java.lang.Object)
     */
    @Override
    public void onOpen(final Object source) {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.net.TcpListener#onResponse(java.lang.Object, byte[])
     */
    @Override
    public void onResponse(final Object source, final byte[] response) {
        // noop
    }
}
