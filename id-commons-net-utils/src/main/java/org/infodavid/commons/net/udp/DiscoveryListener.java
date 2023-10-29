package org.infodavid.commons.net.udp;

import java.net.InetAddress;

import org.infodavid.commons.net.HostDescription;

/**
 * The interface DiscoveryListener.
 */
public interface DiscoveryListener {

    /**
     * Received.
     * @param info the info
     * @param address the address
     */
    void received(HostDescription info, InetAddress address);

    /**
     * Stopped.
     */
    void stopped();

}
