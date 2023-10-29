package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DeletionVisitor.
 */
final class DeletionVisitor extends SimpleFileVisitor<Path> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeletionVisitor.class);

    /** The exception. */
    private IOException exception = null;

    /** The listener. */
    private FileProcessingListener listener = null;

    /**
     * Instantiates a new consumer.
     */
    public DeletionVisitor() {
    }

    /**
     * Instantiates a new consumer.
     * @param listener the listener
     */
    public DeletionVisitor(final FileProcessingListener listener) {
        this.listener = listener;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        try {
            delete(file);
        } catch (@SuppressWarnings("unused") final InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return FileVisitResult.CONTINUE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        try {
            delete(dir);
        } catch (@SuppressWarnings("unused") final InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * Delete.
     * @param path the path
     * @throws InterruptedException the interrupted exception
     */
    private void delete(final Path path) throws InterruptedException {
        Objects.requireNonNull(path);
        LOGGER.trace("Deleting: {}", path.toAbsolutePath());
        int retries = 3;

        try {
            boolean exists = Files.exists(path);

            while (retries > 0 && exists) { // NOSONAR NIO API
                Files.delete(path); // NOSONAR API NIO
                exists = Files.exists(path);

                if (exists) {
                    Thread.sleep(10);
                }

                retries--;
            }

            if (!exists && listener != null) {
                listener.processed(path, FileProcessingListener.DELETED);
            }
        } catch (final IOException e) {
            LOGGER.warn("Deletion failed for: {}", path.toAbsolutePath());

            if (listener == null) {
                exception = e;
            } else {
                try {
                    listener.failed(path, e);
                } catch (final IOException e1) {
                    exception = e1;
                }
            }
        }
    }

    /**
     * Gets the exception.
     * @return the exception
     */
    public IOException getException() {
        return exception;
    }
}
