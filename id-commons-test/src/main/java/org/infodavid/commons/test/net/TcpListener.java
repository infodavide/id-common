package org.infodavid.commons.test.net;

import java.io.IOException;

/**
 * The listener interface.
 * @see TcpEvent
 */
public interface TcpListener {

    /**
     * Handle exception.
     * @param e the exception
     */
    void handleException(Throwable e);

    /**
     * On close.
     * @param source the source
     */
    void onClose(Object source);

    /**
     * On data.
     * @param source  the source
     * @param message the data
     * @return true, if successful processed
     * @throws IOException Signals that an I/O exception has occurred.
     */
    boolean onData(Object source, byte[] message) throws IOException;

    /**
     * On open.
     * @param source the source
     */
    void onOpen(Object source);

    /**
     * On response.
     * @param source   the source
     * @param response the response
     */
    void onResponse(Object source, byte[] response);
}