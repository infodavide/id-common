package org.infodavid.commons.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

/**
 * The Class CompressionUtilities.
 * See types in org.apache.commons.compress.archivers.ArchiveStreamFactory
 */
@SuppressWarnings("static-method")
public final class CompressionUtilities {

    /** The singleton. */
    private static WeakReference<CompressionUtilities> instance = null;

    /** The Constant ZIP. */
    private static final String ZIP = "zip";

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized CompressionUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new CompressionUtilities());
        }

        return instance.get();
    }

    /**
     * Instantiates a new utilities.
     */
    private CompressionUtilities() {
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param out           the out
     * @param level         the level
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final OutputStream out, final byte level, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        compress(path, type, out, level, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param out           the out
     * @param level         the level
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @param listener      the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final OutputStream out, final byte level, final Collection<String> excluded, final boolean includeHidden, final FileProcessingListener listener) throws IOException {
        if (ZIP.equalsIgnoreCase(type)) {
            PathUtilities.getInstance().zip(path, out, level, excluded, includeHidden, listener);

            return;
        }

        final ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        if (Files.isRegularFile(path)) {
            try (ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(type, out)) {
                final ArchiveEntry entry = aos.createArchiveEntry(path, path.getFileName().toString());

                try {
                    aos.putArchiveEntry(entry);
                    Files.copy(path, aos);
                    aos.closeArchiveEntry();
                } catch (final IOException e) {
                    if (listener != null) {
                        listener.failed(path, e);
                    }

                    throw e;          }

                if (listener != null) {
                    listener.processed(path, FileProcessingListener.COMPRESSED);
                }
            } catch (final ArchiveException e) {
                throw new IOException(e);
            }
        }

        final Predicate<Path> filter = new FilterPredicate(Collections.emptyList(), excluded, includeHidden);

        try (ArchiveOutputStream aos = archiveStreamFactory.createArchiveOutputStream(type, out)) {
            PathUtilities.getInstance().walk(path, filter, p -> {
                if (path.equals(p)) {
                    return;
                }

                try {
                    final ArchiveEntry entry = aos.createArchiveEntry(path, path.relativize(p).toString());
                    aos.putArchiveEntry(entry);

                    if (Files.isRegularFile(p)) {
                        Files.copy(p, aos);
                    }

                    aos.closeArchiveEntry();
                } catch (final IOException e) {
                    if (listener != null) {
                        try {
                            listener.failed(p, e);
                        } catch (final IOException e1) {
                            throw new RuntimeException(e1); //NOSONAR Exception in lambda
                        }
                    }

                    throw new RuntimeException(e); //NOSONAR Exception in lambda
                }

                if (listener != null) {
                    listener.processed(p, FileProcessingListener.COMPRESSED);
                }
            });
        } catch (final ArchiveException  e) {
            throw new IOException(e);
        } catch (final Exception e) {
            if (e.getCause() instanceof final IOException ioe) {
                throw ioe;
            }

            throw new IOException(e);
        }
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param out           the out
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final OutputStream out, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        compress(path, type, out, (byte) 1, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param file          the file
     * @param level         the level
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final Path file, final byte level, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        compress(path, type, file, level, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param file          the file
     * @param level         the level
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @param listener      the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final Path file, final byte level, final Collection<String> excluded, final boolean includeHidden, final FileProcessingListener listener) throws IOException {
        PathUtilities.assertReadable(file);

        try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE)) {
            compress(path, type, out, level, excluded, includeHidden, listener);
        }
    }

    /**
     * Compress.
     * @param path          the path
     * @param type          the type
     * @param file          the file
     * @param excluded      the excluded
     * @param includeHidden the include hidden
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void compress(final Path path, final String type, final Path file, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        compress(path, type, file, (byte) 1, excluded, includeHidden);
    }

    /**
     * Extract.
     * @param in       the in
     * @param path     the path
     * @param excluded the excluded
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void extract(final InputStream in, final Path path, final Collection<String> excluded) throws IOException {
        extract(in, path, excluded, new FileProcessingAdapter());
    }

    /**
     * Extract.
     * @param in       the in
     * @param dir      the directory
     * @param excluded the excluded
     * @param listener the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void extract(final InputStream in, final Path dir, final Collection<String> excluded, final FileProcessingListener listener) throws IOException {
        final Predicate<Path> filter = new FilterPredicate(Collections.emptyList(), excluded, true);
        final ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        try (BufferedInputStream bis = new BufferedInputStream(in); ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(bis)) {
            ArchiveEntry entry;

            while ((entry = ais.getNextEntry()) != null) {
                final Path path = dir.resolve(entry.getName());

                if (!filter.test(path)) {
                    continue;
                }

                final Path parent = path.getParent();

                if (!Files.exists(parent)) { // NOSONAR NIO API
                    Files.createDirectories(parent);
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(path);
                } else {
                    Files.copy(ais, path);
                }

                if (listener != null) {
                    listener.processed(path, FileProcessingListener.EXTRACTED);
                }
            }
        } catch (final ArchiveException e) {
            throw new IOException(e);
        }
    }

    /**
     * Extract.
     * @param file     the file
     * @param dir      the directory
     * @param excluded the excluded
     * @param listener the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void extract(final Path file, final Path dir, final Collection<String> excluded, final FileProcessingListener listener) throws IOException {
        PathUtilities.assertReadable(file);
        PathUtilities.assertFile(file);
        final PathUtilities utils = PathUtilities.getInstance();

        if (utils.isValidZipFile(file)) {
            utils.unzip(file, dir, excluded, listener);

            return;
        }

        try (final InputStream in = Files.newInputStream(file)) {
            extract(in, dir, excluded, listener);
        }
    }
}
