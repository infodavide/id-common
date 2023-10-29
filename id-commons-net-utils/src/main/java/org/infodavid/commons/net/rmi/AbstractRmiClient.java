package org.infodavid.commons.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;

/**
 * The Class AbstractRmiClient.</br>
 * First get the registry:</br>
 * </br>
 * <code>Registry registry = LocateRegistry.getRegistry(1099);</code></br>
 * </br>
 * Then, build the client:</br>
 * </br>
 * <code>MyRmiClient client = new MyRmiClient();</code></br>
 * </br>
 * where MyRmiClient is the concrete class based on AbstractRmiClient.</br>
 * Lookup for the server:</br>
 * </br>
 * <code>BasicRmiServer server = (BasicRmiServer) registry.lookup("MyRmiServer");</code></br>
 * </br>
 * where "MyRmiServer" is the name of the RMI service on server side.</br>
 * If you need callbacks, you have to register the client:</br>
 * </br>
 * <code>client.registerTo(server);</code></br>
 * </br>
 * At the end, unbind the server:</br>
 * </br>
 * <code>registry.unbind("MyRmiServer");</code></br>
 */
public abstract class AbstractRmiClient extends UnicastRemoteObject implements BasicRmiClient {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -793812587983939581L;

    static {
        System.setProperty("sun.rmi.registry.registryFilter", "java.**");
    }

    /** The address. */
    private final String address;

    /**
     * Instantiates a new abstract RMI client.
     * @param address the address
     * @throws RemoteException the remote exception
     */
    protected AbstractRmiClient(final String address) throws RemoteException {
        this.address = address;
    }

    /*
     * (non-javadoc)
     * @see java.rmi.server.RemoteObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof final AbstractRmiClient other)) {
            return false;
        }

        return Objects.equals(address, other.address);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.rmi.BasicRmiClient#getAddress()
     */
    @Override
    public String getAddress() throws RemoteException {
        return address;
    }

    /*
     * (non-javadoc)
     * @see java.rmi.server.RemoteObject#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();

        return prime * result + Objects.hash(address);
    }

    /**
     * Register to the server.
     * @param server the server
     * @throws RemoteException the remote exception
     */
    public void registerTo(final BasicRmiServer server) throws RemoteException {
        server.register((BasicRmiClient) RemoteObject.toStub(this));
    }
}
