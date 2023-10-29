package org.infodavid.commons.net.ssl;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * The Class AcceptAllX509TrustManager.
 */
public class AcceptAllX509TrustManager implements X509TrustManager {

    /** The Constant INSTANCE. */
    public static final AcceptAllX509TrustManager INSTANCE = new AcceptAllX509TrustManager();

    /**
     * Instantiates a new accept all trust manager.
     */
    public AcceptAllX509TrustManager() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#checkClientTrusted(java.security.cert. X509Certificate[], java.lang.String)
     */
    @Override
    public void checkClientTrusted(final X509Certificate[] arg0, final String arg1) { // NOSONAR Nothing to do
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#checkServerTrusted(java.security.cert. X509Certificate[], java.lang.String)
     */
    @Override
    public void checkServerTrusted(final X509Certificate[] arg0, final String arg1) { // NOSONAR Nothing to do
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {};
    }
}
