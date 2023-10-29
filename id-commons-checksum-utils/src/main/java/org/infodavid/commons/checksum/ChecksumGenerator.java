package org.infodavid.commons.checksum;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The Interface ChecksumGenerator.
 */
public interface ChecksumGenerator {

    /**
     * Gets the algorithm.
     * @return the algorithm
     */
    String getAlgorithm();

    /**
     * Gets the checksum.
     * @param file the file
     * @return the checksum
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String getChecksum(Path file) throws IOException; // NOSONAR

    /**
     * Gets the checksum.
     * @param content the content
     * @return the checksum
     * @throws IOException Signals that an I/O exception has occurred.
     */
    String getChecksum(String content) throws IOException; // NOSONAR

    /**
     * Checks if is command supported.
     * @return true, if is command supported
     */
    boolean isCommandSupported();
}
