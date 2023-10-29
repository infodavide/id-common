package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 * An asynchronous update interface for receiving notifications about FilesRepository information as the FilesRepository is constructed.
 */
public interface FilesRepositoryObserver {

    /**
     * This method is called when information about an FilesRepository which was previously requested using an asynchronous interface becomes available.
     * @param path the path
     */
    void fileCreated(Path path);

    /**
     * This method is called when information about an FilesRepository which was previously requested using an asynchronous interface becomes available.
     * @param path the path
     */
    void fileDeleted(Path path);

    /**
     * This method is called when information about an FilesRepository which was previously requested using an asynchronous interface becomes available.
     * @param path the path
     */
    void fileModfied(Path path);

    /**
     * This method is called when information about an FilesRepository which was previously requested using an asynchronous interface becomes available.
     * @param repositoryPath the repository path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void initialize(Path repositoryPath) throws IOException;
}
