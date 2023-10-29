package org.infodavid.commons.checksum;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The Class SHA384ChecksumGenerator.
 */
public final class SHA384ChecksumGenerator extends AbstractChecksumGenerator {

    /** The Constant ALGORITHM. */
    public static final String ALGORITHM = "SHA-384";

    /**
     * Instantiates a new SHA 384 checksum generator.
     * @throws InterruptedException the interrupted exception
     */
    public SHA384ChecksumGenerator() throws InterruptedException {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.ChecksumGenerator#getAlgorithm()
     */
    @Override
    public String getAlgorithm() {
        return ALGORITHM;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.ChecksumGenerator#getChecksum(java.lang.String)
     */
    @Override
    public String getChecksum(final String content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("Content is null");
        }

        return DigestUtils.sha384Hex(content);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.AbstractChecksumGenerator#getChecksum(java.io.InputStream)
     */
    @Override
    protected String getChecksum(final InputStream in) throws IOException {
        return DigestUtils.sha384Hex(in);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.AbstractChecksumGenerator#getCommand()
     */
    @Override
    protected String getCommand() {
        return "sha384sum";
    }
}
