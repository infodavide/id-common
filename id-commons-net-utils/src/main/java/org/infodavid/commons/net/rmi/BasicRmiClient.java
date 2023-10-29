package org.infodavid.commons.net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Interface BasicRmiClient.
 */
public interface BasicRmiClient extends Remote {

    /**
     * Gets the address.
     * @return the address
     * @throws RemoteException the remote exception
     */
    String getAddress() throws RemoteException;

    /**
     * On message.
     * @param message the message
     * @throws RemoteException the remote exception
     */
    void onMessage(String message) throws RemoteException;
}
