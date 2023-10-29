package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The Class FileListVisitor.
 */
public class FileListVisitor extends SimpleFileVisitor<Path> {

    /** The excluded. */
    private final Set<String> excluded = new HashSet<>();

    /** The files. */
    private final List<Path> files = new LinkedList<>();

    /** The include hidden. */
    private boolean includeHidden = false;

    /**
     * Instantiates a new visitor.
     */
    public FileListVisitor() {
        // noop
    }

    /**
     * Accept to add the file in the resulting list.
     * @param file the file
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @SuppressWarnings("static-method")
    public boolean accept(final Path file) throws IOException {
        return true;
    }

    /**
     * Gets the excluded.
     * @return the excluded
     */
    public Set<String> getExcluded() {
        return excluded;
    }

    /**
     * Gets the files.
     * @return the files
     */
    public Collection<Path> getFiles() {
        return files;
    }

    /**
     * Checks if is include hidden.
     * @return the includeHidden
     */
    public boolean isIncludeHidden() {
        return includeHidden;
    }

    /*
     * (non-Javadoc)
     * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);

        if (PathUtilities.isExluded(dir, excluded) || !includeHidden && Files.isHidden(dir)) { // NOSONAR Use of NIO API
            return FileVisitResult.SKIP_SUBTREE;
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
     * (non-Javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);

        if (PathUtilities.isExluded(file, excluded) || !Files.isRegularFile(file) || !includeHidden && Files.isHidden(file)) { // NOSONAR Use of NIO API
            return FileVisitResult.CONTINUE;
        }

        if (accept(file)) {
            files.add(file);
        }

        return FileVisitResult.CONTINUE;
    }
}
