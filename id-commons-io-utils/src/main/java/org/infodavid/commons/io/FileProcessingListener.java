package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface FileProcessingListener.
 */
public interface FileProcessingListener {

    /** The processed. */
    byte PROCESSED = 0;

    /** The deleted. */
    byte DELETED = 1;

    /** The copied. */
    byte COPIED = 2;

    /** The moved. */
    byte MOVED = 3;

    /** The replaced. */
    byte REPLACED = 4;

    /** The read. */
    byte READ = 5;

    /** The written. */
    byte WRITTEN = 6;

    /** The compressed. */
    byte COMPRESSED = 7;

    /** The extracted. */
    byte EXTRACTED = 8;

    /** The owner changed. */
    byte OWNER_CHANGED = 9;

    /** The permissions changed. */
    byte PERMISSIONS_CHANGED = 10;

    /** The directory created. */
    byte DIRECTORY_CREATED = 11;

    /** The skipped. */
    byte SKIPPED = 12;

    /**
     * Failed.
     * @param path the path
     * @param e    the exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void failed(final Path path, final IOException e) throws IOException;

    /**
     * Processed.
     * @param path   the path
     * @param action the action
     */
    void processed(Path path, byte action); // NOSONAR Keep name
}
