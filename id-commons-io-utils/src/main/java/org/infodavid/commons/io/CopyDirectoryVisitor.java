package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CopyDirectoryVisitor.
 */
class CopyDirectoryVisitor extends SimpleFileVisitor<Path> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CopyDirectoryVisitor.class);

    /** The excluded. */
    private final Set<String> excluded = new HashSet<>();

    /** The include hidden. */
    private boolean includeHidden = false;

    /** The listener. */
    private FileProcessingListener listener = null;

    /** The move. */
    private final boolean move;

    /** The copy options. */
    private final CopyOption[] options;

    /** The source. */
    private final Path source;

    /** The target. */
    private final Path target;

    /**
     * Instantiates a new visitor.
     * @param source  the source
     * @param target  the target
     * @param move    the move
     * @param options the copy options
     */
    public CopyDirectoryVisitor(final Path source, final Path target, final boolean move, final CopyOption... options) {
        this.move = move;
        this.source = source;
        this.target = target;
        this.options = options;
    }

    /**
     * Instantiates a new visitor.
     * @param source   the source
     * @param target   the target
     * @param move     the move
     * @param listener the listener
     * @param options  the copy option
     */
    public CopyDirectoryVisitor(final Path source, final Path target, final boolean move, final FileProcessingListener listener, final CopyOption... options) {
        this(source, target, move, options);
        this.listener = listener;
    }

    /**
     * Instantiates a new visitor.
     * @param source  the source
     * @param target  the target
     * @param options the copy options
     */
    public CopyDirectoryVisitor(final Path source, final Path target, final CopyOption... options) {
        move = false;
        this.source = source;
        this.target = target;
        this.options = options;
    }

    /**
     * Instantiates a new visitor.
     * @param source   the source
     * @param target   the target
     * @param listener the listener
     * @param options  the copy option
     */
    public CopyDirectoryVisitor(final Path source, final Path target, final FileProcessingListener listener, final CopyOption... options) {
        this(source, target, false, options);
        this.listener = listener;
    }

    /**
     * Gets the excluded.
     * @return the excluded
     */
    public Set<String> getExcluded() {
        return excluded;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        final FileVisitResult result = super.postVisitDirectory(dir, exc);

        if (move && PathUtilities.isEmpty(dir)) {
            Files.deleteIfExists(dir);
        }

        return result;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);

        if (PathUtilities.isExluded(dir, excluded) || !includeHidden && Files.isHidden(dir)) { // NOSONAR Use of NIO API
            if (listener != null) {
                listener.processed(dir, FileProcessingListener.SKIPPED);
            }

            return FileVisitResult.SKIP_SUBTREE;
        }

        final Path targetPath = target.resolve(source.relativize(dir));

        if (!Files.exists(targetPath)) { // NOSONAR API NIO
            LOGGER.debug("Creating directory: {}", targetPath);
            Files.createDirectory(targetPath);
        }

        return FileVisitResult.CONTINUE;
    }

    /**
     * Sets the include hidden.
     * @param includeHidden the includeHidden to set
     */
    public void setIncludeHidden(final boolean includeHidden) {
        this.includeHidden = includeHidden;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);

        if (PathUtilities.isExluded(file, excluded) || !includeHidden && Files.isHidden(file)) { // NOSONAR Use of NIO API
            if (listener != null) {
                listener.processed(file, FileProcessingListener.SKIPPED);
            }

            return FileVisitResult.CONTINUE;
        }

        final Path p = target.resolve(source.relativize(file));

        try {
            if (move) {
                if (Files.isSymbolicLink(file)) {
                    LOGGER.debug("Creating link {} to {}", file, p);
                    Files.createSymbolicLink(p, Files.readSymbolicLink(file));
                    Files.delete(file);
                } else {
                    LOGGER.debug("Moving file {} to {}", file, p);
                    Files.move(file, p, options);
                }

                if (listener != null) {
                    listener.processed(file, FileProcessingListener.MOVED);
                }
            } else {
                if (Files.isSymbolicLink(file)) {
                    LOGGER.debug("Creating link {} to {}", file, p);
                    Files.createSymbolicLink(p, Files.readSymbolicLink(file));
                } else {
                    LOGGER.debug("Copying {} to {}", file, p);
                    Files.copy(file, p, options);
                }

                if (listener != null) {
                    listener.processed(file, FileProcessingListener.COPIED);
                }
            }
        } catch (final IOException e) {
            if (listener == null) {
                throw e;
            }

            listener.failed(file, e);
        }

        return FileVisitResult.CONTINUE;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     */
    @Override
    public FileVisitResult visitFileFailed(final Path path, final IOException e) throws IOException {
        Objects.requireNonNull(path);

        if (listener == null) {
            throw e;
        }

        listener.failed(path, e);

        return FileVisitResult.CONTINUE;
    }
}
