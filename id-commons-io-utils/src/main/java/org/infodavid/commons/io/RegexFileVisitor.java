package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The Class RegexFileVisitor.
 */
public class RegexFileVisitor extends SimpleFileVisitor<Path> {

    /** The matching files. */
    private final List<Path> files = new LinkedList<>();

    /** The pattern. */
    private final Collection<Pattern> patterns;

    /**
     * Instantiates a new file visitor.
     * @param pattern the pattern
     */
    public RegexFileVisitor(final Pattern pattern) {
        patterns = Collections.singleton(pattern);
    }

    /**
     * Instantiates a new file visitor.
     * @param patterns the patterns
     */
    public RegexFileVisitor(final Collection<Pattern> patterns) {
        this.patterns = patterns;
    }

    /**
     * Instantiates a new file visitor.
     * @param expressions the regular expressions
     */
    public RegexFileVisitor(final String... expressions) {
        patterns = new ArrayList<>();

        for (final String expr : expressions) {
            patterns.add(Pattern.compile(expr, Pattern.CASE_INSENSITIVE));
        }
    }

    /**
     * Gets the patterns.
     * @return the patterns
     */
    public Collection<Pattern> getPatterns() {
        return patterns;
    }

    /**
     * Accept to add the file in the resulting list.
     * @param file the file
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean accept(final Path file) throws IOException {
        if (Files.isDirectory(file)) {
            return true;
        }

        for (final Pattern pattern : patterns) {
            if (pattern.matcher(file.getFileName().toString()).matches()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the files.
     * @return the files
     */
    public Collection<Path> getFiles() {
        return files;
    }

    /*
     * (non-javadoc)
     * @see java.nio.file.SimpleFileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     */
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);

        if (accept(file)) {
            files.add(file);
        }

        return FileVisitResult.CONTINUE;
    }
}
