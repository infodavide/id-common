package org.infodavid.commons.net.rmi;

import java.rmi.RemoteException;

/**
 * The Interface BasicRmiServer.
 */
public interface BasicRmiServer extends BasicRmiClient {

    /**
     * Register.
     * @param client the client
     * @throws RemoteException the remote exception
     */
    void register(BasicRmiClient client) throws RemoteException;

    /**
     * Unregister.
     * @param client the client
     * @throws RemoteException the remote exception
     */
    void unregister(BasicRmiClient client) throws RemoteException;

}
