package org.infodavid.commons.net.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;

/**
 * The Class AbstractRmiServer.</br>
 * First you have to create the registry:</br>
 * </br>
 * <code>Registry registry = LocateRegistry.createRegistry(hostname, port);</code></br>
 * </br>
 * Then, instantiate your server object:</br>
 * </br>
 * <code>MyRmiServer server = new MyRmiServer();</code></br>
 * </br>
 * where MyRmiServer is the concrete class based on AbstractRmiServer.</br>
 * Then, you need to publish the RMI service:</br>
 * </br>
 * <code>registry.rebind("MyRmiServer")</code></br>
 */
public abstract class AbstractRmiServer extends UnicastRemoteObject implements BasicRmiServer, RemovalListener<String, BasicRmiClient> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2613042837511564126L;

    /** The address. */
    private final String address;

    /** The cache of clients. */
    private final transient Cache<String, BasicRmiClient> clientsCache;

    /**
     * Instantiates a new abstract RMI server.
     * @param address the address
     * @throws RemoteException the remote exception
     */
    protected AbstractRmiServer(final String address) throws RemoteException {
        this.address = address;
        clientsCache = initializeCache();
    }

    /*
     * (non-Javadoc)
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

        if (!(obj instanceof final AbstractRmiServer other)) {
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
     * (non-Javadoc)
     * @see java.rmi.server.RemoteObject#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();

        return prime * result + Objects.hash(address);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.rmi.BasicRmiServer#register(org.infodavid.commons.net.rmi.BasicRmiClient)
     */
    @Override
    public void register(final BasicRmiClient client) throws RemoteException {
        if (client == null) {
            return;
        }

        getLogger().debug("Registering client: {}", client);
        clientsCache.put(client.getAddress(), client);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.net.rmi.BasicRmiServer#unregister(org.infodavid.commons.net.rmi.BasicRmiClient)
     */
    @Override
    public void unregister(final BasicRmiClient client) throws RemoteException {
        if (client == null) {
            return;
        }

        getLogger().debug("Unregistering client: {}", client);
        clientsCache.invalidate(client.getAddress());
    }

    /**
     * Gets the logger.
     * @return the logger
     */
    protected abstract Logger getLogger();

    /**
     * Initialize cache.
     * @return the cache
     */
    protected Cache<String, BasicRmiClient> initializeCache() {
        return Caffeine.newBuilder().maximumSize(10L).expireAfterAccess(300, TimeUnit.SECONDS).removalListener(this).build();
    }
}
