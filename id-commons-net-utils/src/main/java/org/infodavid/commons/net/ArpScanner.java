package org.infodavid.commons.net;

import org.infodavid.commons.net.udp.DiscoveryListener;

/**
 * The Interface ArpScanner.
 */
public interface ArpScanner {

    /**
     * Discover.
     * @param listener the listener
     * @throws InterruptedException the interrupted exception
     */
    void discover(final DiscoveryListener listener) throws InterruptedException;
}
