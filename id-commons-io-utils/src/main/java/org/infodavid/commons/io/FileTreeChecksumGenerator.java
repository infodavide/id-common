package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The Class FileTreeChecksumGenerator.
 */
public class FileTreeChecksumGenerator implements FileVisitor<Path> {

    /** The buffer. */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * Instantiates a new checksum generator.
     */
    public FileTreeChecksumGenerator() {
        // noop
    }

    /**
     * Gets the checksum.
     * @return the checksum
     */
    public String getChecksum() {
        return DigestUtils.md5Hex(buffer.toString().getBytes(StandardCharsets.UTF_8));
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     */
    /*
     * (non-Javadoc)
     * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    /*
     * (non-Javadoc)
     * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    /*
     * (non-Javadoc)
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        if (Files.exists(file)) { // NOSONAR API NIO
            buffer.append(file.toAbsolutePath().toString());
            buffer.append('\n');
            buffer.append(Files.size(file));
            buffer.append('\n');

            try {
                final String mime = Files.probeContentType(file);

                if (mime != null) {
                    buffer.append(mime);
                }
            } catch (@SuppressWarnings("unused") final Exception e) {
                // noop
            }

            buffer.append('\n');
        }

        return FileVisitResult.CONTINUE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     */
    /*
     * (non-Javadoc)
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exception) throws IOException {
        throw exception;
    }
}
