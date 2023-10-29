package org.infodavid.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class FileTreeMatcher.
 */
class FileTreeMatcher implements FileVisitor<Path> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileTreeMatcher.class);

    /** The checksums. */
    private final StringBuilder checksums = new StringBuilder();

    /** The differences. */
    private final Collection<Path> differences = new ArrayList<>();

    /** The halt on first. */
    private boolean haltOnFirst = true;

    /** The include hidden. */
    private final boolean includeHidden;

    /** The p 1. */
    private final Path p1;

    /** The p 2. */
    private final Path p2;

    /**
     * Instantiates a new file tree matcher.
     * @param p1          the p 1
     * @param p2          the p 2
     * @param haltOnFirst the halt on first
     */
    public FileTreeMatcher(final Path p1, final Path p2, final boolean haltOnFirst) {
        this(p1, p2, haltOnFirst, false);
    }

    /**
     * Instantiates a new file tree matcher.
     * @param p1            the p 1
     * @param p2            the p 2
     * @param haltOnFirst   the halt on first
     * @param includeHidden the include hidden files or directories
     */
    public FileTreeMatcher(final Path p1, final Path p2, final boolean haltOnFirst, final boolean includeHidden) {
        this.haltOnFirst = haltOnFirst;
        this.includeHidden = includeHidden;
        this.p1 = p1.toAbsolutePath();
        this.p2 = p2.toAbsolutePath();
    }

    /**
     * Gets the checksum.
     * @return the checksum
     */
    public String getChecksum() {
        return DigestUtils.md5Hex(checksums.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gets the differences.
     * @return the differences
     */
    public Collection<Path> getDifferences() {
        return differences;
    }

    /*
     * (non-javadoc)
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
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        final Path relativeDir = p1.relativize(dir.toAbsolutePath());
        final Path actualDir = p2.resolve(relativeDir);

        if (PathUtilities.isHidden(dir) && !includeHidden) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        if (actualDir == null || !Files.exists(actualDir)) { // NOSONAR API NIO
            LOGGER.debug("Folder does not exist: {}", actualDir);
            differences.add(dir);
        } else {
            final AtomicInteger left = new AtomicInteger(0);

            try (Stream<Path> stream = Files.list(dir)) {
                stream.forEach(p -> {
                    if (includeHidden || !PathUtilities.isHidden(p)) {
                        left.incrementAndGet();
                    }
                });
            }

            final AtomicInteger right = new AtomicInteger(0);

            try (Stream<Path> stream = Files.list(actualDir)) {
                stream.forEach(p -> {
                    if (includeHidden || !PathUtilities.isHidden(p)) {
                        right.incrementAndGet();
                    }
                });
            }

            if (left.get() != right.get()) {
                LOGGER.debug("Children count of {} and {} are not the same", actualDir, dir);
                differences.add(dir);
            }
        }

        return differences.isEmpty() || !haltOnFirst ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);
        final Path relativeFile = p1.relativize(file.toAbsolutePath());
        final Path actualFile = p2.resolve(relativeFile);

        if (PathUtilities.isHidden(file) && !includeHidden) {
            return FileVisitResult.CONTINUE;
        }

        if (!Files.exists(actualFile)) { // NOSONAR API NIO
            LOGGER.debug("File does not exist: {}", actualFile);
            differences.add(file);
        } else if (Files.size(file) != Files.size(actualFile)) { // NOSONAR API NIO
            LOGGER.debug("Sizes of files {} and {} are not the same", actualFile, file);
            differences.add(file);
        } else {
            final String checksum = getChecksum(file);

            if (!checksum.equals(getChecksum(actualFile))) {
                LOGGER.debug("Checksums of files {} and {} are not the same", actualFile, file);
                differences.add(file);
            }

            checksums.append(checksum);
        }

        return differences.isEmpty() || !haltOnFirst ? FileVisitResult.CONTINUE : FileVisitResult.TERMINATE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exception) throws IOException {
        throw exception;
    }

    /**
     * Gets the checksum.
     * @param file the file
     * @return the checksum
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String getChecksum(final Path file) throws IOException {
        if (Files.isDirectory(file)) { // NOSONAR API NIO
            throw new IllegalArgumentException("Not a regular file: " + file.toAbsolutePath());
        }

        try (InputStream in = Files.newInputStream(file)) {
            return DigestUtils.md5Hex(in);
        }
    }
}
