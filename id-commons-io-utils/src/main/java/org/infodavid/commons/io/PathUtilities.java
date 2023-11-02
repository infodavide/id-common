package org.infodavid.commons.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntry.Builder;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.apache.commons.io.FileUtils;
import org.infodavid.commons.system.SystemUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PathUtilities.
 */
@SuppressWarnings("static-method")
public final class PathUtilities {

    /**
     * The Class DeleteOnExitRunnable.
     */
    private static class DeleteOnExitRunnable implements Runnable {

        /** The paths. */
        private final Set<Path> paths = new HashSet<>();

        /**
         * Gets the paths.
         * @return the paths
         */
        public Set<Path> getPaths() {
            return paths;
        }

        /*
         * (non-javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            final PathUtilities utils = PathUtilities.getInstance();

            for (final Path path : paths) {
                utils.deleteQuietly(path);
            }
        }
    }

    /** The Constant DELETE_ON_EXIT_RUNNABLE. */
    private static final DeleteOnExitRunnable DELETE_ON_EXIT_RUNNABLE;

    /** The Constant EXECUTE_ACL_ENTRY_PERMISSIONS. */
    public static final Set<AclEntryPermission> EXECUTE_ACL_ENTRY_PERMISSIONS;

    /** The Constant FILE_NOT_FOUND. */
    private static final String FILE_NOT_FOUND = "File not found: ";

    /** The singleton. */
    private static WeakReference<PathUtilities> instance = null;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PathUtilities.class);

    /** The Constant OS_SPECIFIC_EXCLUDED_SET. */
    public static final Set<String> OS_SPECIFIC_EXCLUDED_SET;

    /** The Constant OTHERS_PRINCIPAL. */
    public static final GroupPrincipal OTHERS_PRINCIPAL = new GroupPrincipal() {

        /*
         * (non-javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (!(obj instanceof final GroupPrincipal other)) {
                return false;
            }

            return getName().equals(other.getName());
        }

        @Override
        public String getName() {
            return "others";
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return getName().hashCode();
        }

        /*
         * (non-javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return getName();
        }
    };

    /** The Constant READ_ACL_ENTRY_PERMISSIONS. */
    public static final Set<AclEntryPermission> READ_ACL_ENTRY_PERMISSIONS;

    /** The Constant VERSION_CONTROL_EXCLUDED_SET. */
    public static final Set<String> VERSION_CONTROL_EXCLUDED_SET;

    /** The Constant WRITE_ACL_ENTRY_PERMISSIONS. */
    public static final Set<AclEntryPermission> WRITE_ACL_ENTRY_PERMISSIONS;

    static {
        HashSet<String> set = new HashSet<>();
        set.add(".DS_Store");
        set.add(".Trashes");
        set.add(".Trash");
        set.add("Thumbs.db");
        set.add("Desktop.ini");
        set.add(".lock");
        set.add(".tmp");
        OS_SPECIFIC_EXCLUDED_SET = Collections.unmodifiableSet(set);
        set = new HashSet<>();
        set.add(".bzr");
        set.add(".svn");
        set.add(".hg");
        set.add(".fslckout");
        set.add("_FOSSIL_");
        set.add(".fos");
        set.add("CVS");
        set.add("_darcs");
        set.add(".git");
        set.add(".osc");
        VERSION_CONTROL_EXCLUDED_SET = Collections.unmodifiableSet(set);
        Set<AclEntryPermission> permissions = new HashSet<>();
        permissions.add(AclEntryPermission.READ_DATA);
        permissions.add(AclEntryPermission.READ_ACL);
        permissions.add(AclEntryPermission.READ_ATTRIBUTES);
        permissions.add(AclEntryPermission.READ_NAMED_ATTRS);
        permissions.add(AclEntryPermission.LIST_DIRECTORY);
        READ_ACL_ENTRY_PERMISSIONS = Collections.unmodifiableSet(permissions);
        permissions = new HashSet<>();
        permissions.add(AclEntryPermission.WRITE_DATA);
        permissions.add(AclEntryPermission.APPEND_DATA);
        permissions.add(AclEntryPermission.WRITE_ACL);
        permissions.add(AclEntryPermission.WRITE_ATTRIBUTES);
        permissions.add(AclEntryPermission.WRITE_NAMED_ATTRS);
        permissions.add(AclEntryPermission.DELETE);
        permissions.add(AclEntryPermission.ADD_FILE);
        permissions.add(AclEntryPermission.ADD_SUBDIRECTORY);
        permissions.add(AclEntryPermission.DELETE_CHILD);
        WRITE_ACL_ENTRY_PERMISSIONS = Collections.unmodifiableSet(permissions);
        permissions = new HashSet<>();
        permissions.add(AclEntryPermission.EXECUTE);
        EXECUTE_ACL_ENTRY_PERMISSIONS = Collections.unmodifiableSet(permissions);
    }

    static {
        DELETE_ON_EXIT_RUNNABLE = new DeleteOnExitRunnable();
        Runtime.getRuntime().addShutdownHook(new Thread(DELETE_ON_EXIT_RUNNABLE));
    }

    /**
     * Assert directory.
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void assertDirectory(final Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            throw new IOException(path.toString() + " is not a directory.");
        }
    }

    /**
     * Assert file.
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void assertFile(final Path path) throws IOException {
        if (!Files.isRegularFile(path)) {
            throw new IOException(path.toString() + " is not a regular file.");
        }
    }

    /**
     * Assert readable.
     * @param path the path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void assertReadable(final Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new NoSuchFileException(path.toString());
        }

        if (!Files.isReadable(path)) {
            throw new IOException(path.toString() + " is not readable.");
        }
    }

    /**
     * returns the singleton.
     * @return the singleton
     */
    public static synchronized PathUtilities getInstance() {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new PathUtilities());
        }

        return instance.get();
    }

    /**
     * Checks if is empty.
     * @param path the path
     * @return true, if is empty
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static boolean isEmpty(final Path path) throws IOException {
        boolean result = false;

        if (Files.isDirectory(path)) { // NOSONAR NIO API
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                result = !stream.iterator().hasNext();
            }
        } else if (Files.exists(path)) { // NOSONAR NIO API
            result = Files.size(path) == 0;
        } else {
            result = true;
        }

        return result;
    }

    /**
     * Checks if is excluded.
     * @param path     the path
     * @param excluded the excluded files or directories
     * @return true, if is excluded
     */
    public static boolean isExluded(final Path path, final Collection<String> excluded) {
        if (path == null) {
            return true;
        }

        Path current = path;

        do {
            if (excluded != null && (excluded.contains(current.getFileName().toString()) || excluded.contains(current.toAbsolutePath().toString()))) {
                return true;
            }

            current = current.getParent();
        } while (current != null && current.getFileName() != null);

        return false;
    }

    /**
     * Checks if is hidden.<br>
     * Static to allow use in streams.
     * @param path the path
     * @return true, if is hidden
     */
    public static boolean isHidden(final Path path) {
        if (path == null) {
            return true;
        }

        if (path.getFileName() == null) {
            return false;
        }

        try {
            return Files.isHidden(path) || path.getFileName().toString().startsWith(".");
        } catch (@SuppressWarnings("unused") final IOException e) {
            return false;
        }
    }

    /**
     * Checks if is hidden.<br>
     * Static to allow use in streams.
     * @param path the path
     * @return true, if is hidden
     */
    public static boolean isTreeHidden(final Path path) {
        if (path == null) {
            return true;
        }

        Path current = path;

        do {
            if (isHidden(current)) {
                return true;
            }

            current = current.getParent();
        } while (current != null && current.getFileName() != null);

        return false;
    }

    /**
     * Instantiates a new utilities.
     */
    private PathUtilities() {
    }

    /**
     * Compare two folders.
     * @param p1 first folder
     * @param p2 second folder
     * @return true, if identical
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean compare(final Path p1, final Path p2) throws IOException { // NOSONAR
        return compare(p1, p2, false);
    }

    /**
     * Compare two folders.
     * @param p1            first folder
     * @param p2            second folder
     * @param includeHidden the include hidden
     * @return true, if identical
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean compare(final Path p1, final Path p2, final boolean includeHidden) throws IOException { // NOSONAR
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Comparaison de : {} et : {}", p1, p2);
        }

        if (p1 == p2) { // NOSONAR
            return true;
        }

        if (p1 == null || p2 == null) { // NOSONAR
            return false;
        }

        if (!includeHidden && (isHidden(p1) || isHidden(p2))) {
            return true;
        }

        if (p1.getFileName() == null || !Files.exists(p1) || p2.getFileName() == null || !Files.exists(p2)) { // NOSONAR NIO API
            return false;
        }

        if (p1.toRealPath() == p2.toRealPath()) { // NOSONAR NIO API
            return true;
        }

        if (Files.isDirectory(p1) && !Files.isDirectory(p2)) { // NOSONAR API NIO
            final Path p = p1.resolve(p2.getFileName().toString());

            return equals(p, p2);
        }

        if (!Files.isDirectory(p1) && Files.isDirectory(p2)) { // NOSONAR Utilisation de l'API NIO
            final Path p = p2.resolve(p1.getFileName().toString());

            return equals(p, p2);
        }

        final FileTreeMatcher matcher = new FileTreeMatcher(p1, p2, true, includeHidden);

        Files.walkFileTree(p1, matcher);

        return matcher.getDifferences().isEmpty();
    }

    /**
     * Move copy or directory.<br/>
     * If target is a directory and if it exists, content of source is copied into the existing directory.<br/>
     * If target is a directory and if it does not exist, source is copied into the new target directory.
     * @param from the path
     * @param to   the destination path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void copy(final Path from, final Path to) throws IOException {
        copy(from, to, Collections.emptySet(), false, null);
    }

    /**
     * Move copy or directory.<br/>
     * If target is a directory and if it exists, content of source is copied into the existing directory.<br/>
     * If target is a directory and if it does not exist, source is copied into the new target directory.
     * @param from    the path
     * @param to      the destination path
     * @param options the options
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void copy(final Path from, final Path to, final CopyOption... options) throws IOException {
        copy(from, to, Collections.emptySet(), false, null, options);
    }

    /**
     * Move copy or directory.
     * @param from          the path
     * @param to            the destination path
     * @param excluded      the excluded
     * @param includeHidden true to include hidden
     * @param listener      the listener
     * @param options       the options
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void copy(final Path from, final Path to, final Collection<String> excluded, final boolean includeHidden, final FileProcessingListener listener, final CopyOption... options) throws IOException {
        if (Files.isRegularFile(from)) { // NOSONAR API NIO
            if (!Files.isRegularFile(from)) { // NOSONAR API NIO
                throw new IllegalArgumentException("Source is not a regular file: " + from);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Copying file {} to {}", from, to.toAbsolutePath());
            }

            Files.copy(from, to, options);

            if (listener != null) {
                listener.processed(from, FileProcessingListener.COPIED);
            }
        } else if (Files.isSymbolicLink(from)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating link {} to {}", from, to.toAbsolutePath());
            }

            Files.createSymbolicLink(to, Files.readSymbolicLink(from));

            if (listener != null) {
                listener.processed(from, FileProcessingListener.COPIED);
            }
        } else if (Files.isDirectory(from)) { // NOSONAR API NIO
            if (!Files.exists(to)) { // NOSONAR API NIO
                LOGGER.debug("Creating directory: {}", to);

                Files.createDirectories(to.toAbsolutePath());
            } else if (!Files.isDirectory(to)) { // NOSONAR API NIO
                throw new IllegalArgumentException("Target exists but is not a directory: " + to);
            }

            final CopyDirectoryVisitor visitor = new CopyDirectoryVisitor(from.toAbsolutePath(), to.toAbsolutePath(), listener, options);
            visitor.getExcluded().addAll(excluded);
            visitor.setIncludeHidden(includeHidden);
            Files.walkFileTree(from.toAbsolutePath(), visitor);

            if (listener != null) {
                listener.processed(from, FileProcessingListener.COPIED);
            }
        } else {
            throw new IllegalArgumentException("Source is not a file or a directory: " + from);
        }
    }

    /**
     * Delete.
     * @param path the path
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean delete(final Path path) throws IOException {
        return delete(path, null);
    }

    /**
     * Delete.
     * @param path     the path
     * @param listener the listener
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean delete(final Path path, final FileProcessingListener listener) throws IOException {
        LOGGER.debug("Deleting: {}", path);

        if (path == null) {
            return false;
        }

        if (Files.isDirectory(path)) { // NOSONAR API NIO
            final DeletionVisitor visitor = new DeletionVisitor(listener);
            Files.walkFileTree(path, visitor);

            if (visitor.getException() != null) {
                throw visitor.getException();
            }
        }

        boolean exists = Files.exists(path);
        int retries = 3;

        while (retries > 0 && exists) { // NOSONAR NIO API
            Files.delete(path); // NOSONAR API NIO
            exists = Files.exists(path);

            if (exists) {
                try {
                    Thread.sleep(10);
                } catch (@SuppressWarnings("unused") final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            retries--;
        }

        if (exists) {
            return false;
        }

        if (listener != null) {
            listener.processed(path, FileProcessingListener.DELETED);
        }

        return true;
    }

    /**
     * Delete on exit.
     * @param path the path
     */
    public void deleteOnExit(final Path path) {
        DELETE_ON_EXIT_RUNNABLE.getPaths().add(path);
    }

    /**
     * Delete quietly.
     * @param path the path
     * @return true, if successful
     */
    public boolean deleteQuietly(final Path path) {
        if (path == null) {
            return false;
        }

        try {
            return delete(path);
        } catch (final Exception ignored) { // NOSONAR Quietly
            LOGGER.trace("Deletion error", ignored);

            return false;
        }
    }

    /**
     * Equals.
     * @param p1 the left path
     * @param p2 the right path
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private boolean equals(final Path p1, final Path p2) throws IOException {
        String left;

        try (InputStream in = Files.newInputStream(p1)) {
            left = DigestUtils.md5Hex(in);
        }

        String right;

        try (InputStream in = Files.newInputStream(p2)) {
            right = DigestUtils.md5Hex(in);
        }

        return left.equals(right);
    }

    /**
     * Gets the group principal.
     * @param path the path
     * @return the group principal
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public GroupPrincipal getGroup(final Path path) throws IOException {
        if (!Files.exists(path)) { // NOSONAR NIO API
            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        try {
            final PosixFileAttributes attributes = Files.readAttributes(path, PosixFileAttributes.class);

            return attributes == null ? null : attributes.group();
        } catch (@SuppressWarnings("unused") final UnsupportedOperationException e) {
            LOGGER.debug("Cannot retrieve group principal for path: {}", path);
        }

        return null;
    }

    /**
     * Gets the human readable size.
     * @param size the size
     * @return the human readable size
     */
    public String getHumanReadableSize(final long size) {
        return getHumanReadableSize(size, true);
    }

    /**
     * Gets the human readable size.
     * @param size the size
     * @param si   true to use 1000
     * @return the human readable size
     */
    @SuppressWarnings("boxing")
    public String getHumanReadableSize(final long size, final boolean si) {
        final int unit = si ? 1000 : 1024;

        if (size < unit) {
            return size + " B";
        }

        final int exp = (int) (Math.log(size) / Math.log(unit));
        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", size / Math.pow(unit, exp), pre);
    }

    /**
     * Gets the human readable size.
     * @param file the file
     * @param si   true to use 1000
     * @return the human readable size
     * @deprecated see {@link PathUtilities#getHumanReadableSize(long, boolean)}
     */
    @Deprecated
    public String getHumanReadableSize(final Path file, final boolean si) {
        if (Files.isDirectory(file)) { // NOSONAR API NIO
            return "-1";
        }

        long bytes;

        try {
            bytes = Files.size(file);
        } catch (final IOException e) {
            LOGGER.trace("Cannot read file size", e);

            return "-1";
        }

        return getHumanReadableSize(bytes, si);
    }

    /**
     * Gets the user principal.
     * @param path the path
     * @return the user principal
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public UserPrincipal getOwner(final Path path) throws IOException {
        if (!Files.exists(path)) { // NOSONAR NIO API
            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        return Files.getOwner(path);
    }

    /**
     * Gets the permissions.
     * @param path the path
     * @return the permissions
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Set<AclEntry> getPermissions(final Path path) throws IOException {
        if (path == null) {
            return Collections.emptySet();
        }

        if (!Files.exists(path)) { // NOSONAR NIO API
            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        final AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);

        if (view == null) {
            LOGGER.debug("ACL are not supported, getting POSIX permissions.");

            return getPermissionsFromPosixPermissions(path);
        }

        final Set<AclEntry> results = new HashSet<>();
        final List<AclEntry> entries = view.getAcl();

        if (entries != null) {
            results.addAll(entries);
        }

        return results;
    }

    /**
     * Gets the permissions from POSIX permissions.
     * @param path the path
     * @return the permissions from POSIX permissions
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private Set<AclEntry> getPermissionsFromPosixPermissions(final Path path) throws IOException {
        final UserPrincipal ownerPrincipal = getOwner(path);
        final GroupPrincipal groupPrincipal = getGroup(path);
        final PosixFileAttributes attributes = Files.readAttributes(path, PosixFileAttributes.class);

        if (attributes == null) {
            return Collections.emptySet();
        }

        final Set<PosixFilePermission> permissions = attributes.permissions();

        if (permissions == null) {
            return Collections.emptySet();
        }

        final Set<AclEntry> results = new HashSet<>();
        final Set<AclEntryPermission> ownerPermissions = new HashSet<>();
        final Set<AclEntryPermission> groupPermissions = new HashSet<>();
        final Set<AclEntryPermission> otherPermissions = new HashSet<>();

        for (final PosixFilePermission p : permissions) {
            if (PosixFilePermission.OWNER_READ.equals(p)) {
                ownerPermissions.addAll(READ_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.OWNER_WRITE.equals(p)) {
                ownerPermissions.addAll(WRITE_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.OWNER_EXECUTE.equals(p)) {
                ownerPermissions.addAll(EXECUTE_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.GROUP_READ.equals(p)) {
                groupPermissions.addAll(READ_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.GROUP_WRITE.equals(p)) {
                groupPermissions.addAll(WRITE_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.GROUP_EXECUTE.equals(p)) {
                groupPermissions.addAll(EXECUTE_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.OTHERS_READ.equals(p)) {
                otherPermissions.addAll(READ_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.OTHERS_WRITE.equals(p)) {
                otherPermissions.addAll(WRITE_ACL_ENTRY_PERMISSIONS);
            } else if (PosixFilePermission.OTHERS_EXECUTE.equals(p)) {
                otherPermissions.addAll(EXECUTE_ACL_ENTRY_PERMISSIONS);
            }
        }

        final Builder builder = AclEntry.newBuilder();

        if (ownerPrincipal != null && !ownerPermissions.isEmpty()) {
            results.add(builder.setPrincipal(ownerPrincipal).setType(AclEntryType.ALLOW).setPermissions(ownerPermissions).build());
        }

        if (groupPrincipal != null && !groupPermissions.isEmpty()) {
            results.add(builder.setPrincipal(groupPrincipal).setType(AclEntryType.ALLOW).setPermissions(groupPermissions).build());
        }

        if (!otherPermissions.isEmpty()) {
            results.add(builder.setPrincipal(OTHERS_PRINCIPAL).setType(AclEntryType.ALLOW).setPermissions(otherPermissions).build());
        }

        return results;
    }

    /**
     * Gets the size.
     * @param path      the path
     * @param recursive the recursive
     * @return the size
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public long getSize(final Path path, final boolean recursive) throws IOException {
        if (!Files.isDirectory(path)) { // NOSONAR API NIO
            return Files.size(path);
        }

        final DirectorySizeVisitor visitor = new DirectorySizeVisitor(path, recursive);
        Files.walkFileTree(path, visitor);

        return visitor.getSize();
    }

    /**
     * Gets the temporary directory.
     * @return the directory
     */
    public String getTempDirectory() {
        return FileUtils.getTempDirectoryPath();
    }

    /**
     * Gets the temporary directory.
     * @return the directory
     */
    public Path getTempPath() {
        return Paths.get(getTempDirectory());
    }

    /**
     * Gets the user directory.
     * @return the user directory
     */
    public String getUserDirectory() {
        return FileUtils.getUserDirectoryPath();
    }

    /**
     * Gets the user directory.
     * @return the user directory
     */
    public Path getUserPath() {
        return Paths.get(getUserDirectory());
    }

    /**
     * Checks if is child.
     * @param parent the parent
     * @param path   the path
     * @return true, if is child
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public boolean isChild(final Path parent, final Path path) throws IOException {
        if (parent == null || path == null || !Files.exists(parent)) { // NOSONAR NIO API
            return false;
        }

        Path p = path;

        do {
            if (Files.exists(p) && Files.isSameFile(parent, p)) { // NOSONAR NIO API
                return true;
            }

            if (p == p.getParent()) {
                p = null;
            } else {
                p = p.getParent();
            }
        } while (p != null);

        return false;
    }

    /**
     * Checks if is valid ZIP file.
     * @param file the file
     * @return true, if is valid ZIP file
     */
    public boolean isValidZipFile(final Path file) {
        int signature = 0;

        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
            signature = raf.readInt();
        } catch (@SuppressWarnings("unused") final IOException e) {
            // noop
        }

        return signature == 0x504B0304 || signature == 0x504B0506 || signature == 0x504B0708;
    }

    /**
     * Move file or directory.<br/>
     * If target is a directory and if it exists, content of source is copied into the existing directory.<br/>
     * If target is a directory and if it does not exist, source is moved normally.
     * @param from the path
     * @param to   the destination path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void move(final Path from, final Path to) throws IOException {
        move(from, to, (FileProcessingListener) null);
    }

    /**
     * Move file or directory.<br/>
     * If target is a directory and if it exists, content of source is copied into the existing directory.<br/>
     * If target is a directory and if it does not exist, source is moved normally.
     * @param from    the path
     * @param to      the destination path
     * @param options the options
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void move(final Path from, final Path to, final CopyOption... options) throws IOException {
        move(from, to, null, options);
    }

    /**
     * Move file or directory.
     * @param from     the path
     * @param to       the destination path
     * @param listener the listener
     * @param options  the options
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void move(final Path from, final Path to, final FileProcessingListener listener, final CopyOption... options) throws IOException {
        if (Files.isRegularFile(from)) { // NOSONAR API NIO
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Moving file {} to {}", from, to.toAbsolutePath());
            }

            Files.move(from, to, options);

            if (listener != null) {
                listener.processed(from, FileProcessingListener.MOVED);
            }
        } else if (Files.isSymbolicLink(from)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating link {} to {}", from, to.toAbsolutePath());
            }

            Files.createSymbolicLink(to, Files.readSymbolicLink(from));
            Files.delete(from);

            if (listener != null) {
                listener.processed(from, FileProcessingListener.MOVED);
            }
        } else if (Files.isDirectory(from)) { // NOSONAR API NIO
            if (!Files.exists(to)) { // NOSONAR NIO API
                Files.createDirectories(to);
            }

            if (!Files.isDirectory(to)) { // NOSONAR API NIO
                throw new IllegalArgumentException("Target exists but is not a directory: " + to);
            }

            final CopyDirectoryVisitor visitor = new CopyDirectoryVisitor(from.toAbsolutePath(), to.toAbsolutePath(), true, listener, options);
            Files.walkFileTree(from.toAbsolutePath(), visitor);

            if (listener != null) {
                listener.processed(from, FileProcessingListener.COPIED);
            }

            if (isEmpty(from)) {
                delete(from);
            }
        } else {
            throw new IllegalArgumentException("Source type is not handled: " + from);
        }
    }

    /**
     * Build a string representation of the files tree.
     * @param folder the path
     * @return the string representation
     */
    public String printDirectoryTree(final Path folder) {
        return printDirectoryTree(folder, false);
    }

    /**
     * Build a string representation of the files tree.
     * @param folder        the path
     * @param includeHidden true to include hidden files
     * @return the string representation
     */
    public String printDirectoryTree(final Path folder, final boolean includeHidden) {
        if (folder == null || folder.getFileName() == null) {
            return "";
        }

        if (!Files.isDirectory(folder)) { // NOSONAR API NIO
            return folder.getFileName().toString();
        }

        final int indent = 0;
        final StringBuilder buffer = new StringBuilder();
        printDirectoryTree(folder, includeHidden, indent, buffer);

        return buffer.toString();
    }

    /**
     * Build a string representation of the files tree.
     * @param folder        the path
     * @param includeHidden true to include hidden files
     * @param indent        the number of spaces to use as indentation
     * @param buffer        the buffer
     */
    public void printDirectoryTree(final Path folder, final boolean includeHidden, final int indent, final StringBuilder buffer) {
        if (!Files.isDirectory(folder)) { // NOSONAR API NIO
            return;
        }

        for (int i = 0; i < indent; i++) {
            buffer.append("|  ");
        }

        buffer.append("+--");
        buffer.append(folder.getFileName());
        buffer.append("/");
        buffer.append("\n");

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            stream.forEach(p -> {
                if (isHidden(p) && !includeHidden) {
                    return;
                }

                if (Files.isDirectory(p)) { // NOSONAR API NIO
                    printDirectoryTree(p, indent + 1, buffer);
                } else {
                    printFile(p, indent + 1, buffer);
                }
            });
        } catch (final IOException e) {
            LOGGER.debug("An error occured while listing directory: {} ({})", folder, e.getMessage()); // NOSONAR Not with a throwable
        }
    }

    /**
     * Build a textual view of the the file tree.
     * @param folder the path
     * @param indent the indentation
     * @param buffer the buffer to use
     */
    public void printDirectoryTree(final Path folder, final int indent, final StringBuilder buffer) {
        printDirectoryTree(folder, false, indent, buffer);
    }

    /**
     * Prints the file.
     * @param file   the file
     * @param indent the indent
     * @param buffer the buffer
     */
    private void printFile(final Path file, final int indent, final StringBuilder buffer) {
        for (int i = 0; i < indent; i++) {
            buffer.append("|  ");
        }

        buffer.append("+--");
        buffer.append(file.getFileName());
        buffer.append(" (");
        buffer.append(getHumanReadableSize(file, false));
        buffer.append(")\n");
    }

    /**
     * Sets the executable.
     * @param path the new executable
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setExecutable(final Path path) throws IOException {
        final Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        Files.setPosixFilePermissions(path, perms);
    }

    /**
     * Sets the owner without recursive processing.
     * @param path  the path
     * @param owner the owner
     * @param group the group
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setOwner(final Path path, final String owner, final String group) throws IOException {
        setOwner(path, owner, group, false, null);
    }

    /**
     * Sets the owner.
     * @param path      the path
     * @param owner     the owner
     * @param group     the group
     * @param recursive the recursive
     * @param listener  the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setOwner(final Path path, final String owner, final String group, final boolean recursive, final FileProcessingListener listener) throws IOException {
        if (!Files.exists(path)) { // NOSONAR NIO API
            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        final SystemUtilities utils = SystemUtilities.getInstance();
        final UserPrincipal ownerPrincipal = utils.getUserPrincipal(owner);
        final GroupPrincipal groupPrincipal = utils.getGroupPrincipal(group);
        setOwner(path, ownerPrincipal, groupPrincipal, recursive, listener);
    }

    /**
     * Sets the owner without recursive processing.
     * @param path     the path
     * @param owner    the owner
     * @param group    the group
     * @param listener the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setOwner(final Path path, final String owner, final String group, final FileProcessingListener listener) throws IOException {
        setOwner(path, owner, group, false, listener);
    }

    /**
     * Sets the owner.
     * @param path      the path
     * @param owner     the owner
     * @param group     the group
     * @param recursive the recursive
     * @param listener  the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setOwner(final Path path, final UserPrincipal owner, final GroupPrincipal group, final boolean recursive, final FileProcessingListener listener) throws IOException {
        if (!Files.exists(path)) { // NOSONAR NIO API
            if (listener != null) {
                listener.processed(path, FileProcessingListener.SKIPPED);
            }

            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        LOGGER.debug("Setting owner for path: {} to {} ({})", path, owner, group);

        try {
            final PosixFileAttributeView view = Files.getFileAttributeView(path, PosixFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);

            if (view == null) {
                throw new UnsupportedOperationException();
            }

            view.setOwner(owner);

            if (group == null) {
                if (listener != null) {
                    listener.processed(path, FileProcessingListener.SKIPPED);
                }
            } else {
                view.setGroup(group);
            }
        } catch (@SuppressWarnings("unused") final UnsupportedOperationException e) {
            try {
                Files.setOwner(path, owner);
            } catch (final IOException e2) {
                if (listener != null) {
                    listener.failed(path, e2);
                }

                throw e2;
            }
        }

        if (listener != null) {
            listener.processed(path, FileProcessingListener.OWNER_CHANGED);
        }

        if (recursive && Files.isDirectory(path)) { // NOSONAR NIO API
            try (final Stream<Path> stream = Files.walk(path, 1)) {
                stream.filter(p -> !Files.isSymbolicLink(p)).forEach(p -> {
                    try {
                        setOwner(p, owner, group, recursive, listener);
                    } catch (final IOException e) {
                        if (listener == null) {
                            throw new RuntimeException(e); // NOSONAR Used in stream
                        }

                        try {
                            listener.failed(p, e);
                        } catch (final IOException e1) {
                            throw new RuntimeException(e1); // NOSONAR Used in stream
                        }
                    }
                });
            } catch (final RuntimeException e) {
                IOException cause;

                if (e.getCause() instanceof final IOException e2) {
                    cause = e2;
                } else {
                    cause = new IOException(e);
                }

                throw cause;
            }
        }
    }

    /**
     * Sets the permissions without recursive processing.
     * @param path the path
     * @param acl  the ACL
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setPermissions(final Path path, final Set<AclEntry> acl) throws IOException {
        setPermissions(path, acl, false, null);
    }

    /**
     * Sets the permissions.
     * @param path      the path
     * @param acl       the ACL
     * @param recursive the recursive
     * @param listener  the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setPermissions(final Path path, final Set<AclEntry> acl, final boolean recursive, final FileProcessingListener listener) throws IOException {
        if (!Files.exists(path)) { // NOSONAR NIO API
            if (listener != null) {
                listener.processed(path, FileProcessingListener.SKIPPED);
            }

            throw new NoSuchFileException(FILE_NOT_FOUND + path.toString());
        }

        final Set<AclEntry> specific = new HashSet<>(acl);

        if (!Files.isDirectory(path)) { // NOSONAR NIO API
            for (final AclEntry entry : specific) {
                entry.permissions().remove(AclEntryPermission.DELETE_CHILD);
            }
        }

        LOGGER.debug("Setting permissions for path: {} to {}", path, specific);
        final AclFileAttributeView view = Files.getFileAttributeView(path, AclFileAttributeView.class);

        if (view == null) {
            LOGGER.debug("ACL are not supported, setting POSIX permissions.");

            try {
                setPosixPermissions(path, specific);
            } catch (final IOException e) {
                if (listener != null) {
                    listener.failed(path, e);
                }

                throw e;
            }
        } else {
            final SetValuedMap<UserPrincipal, AclEntry> entries = new HashSetValuedHashMap<>();

            for (final AclEntry entry : view.getAcl()) {
                entries.put(entry.principal(), entry);
            }

            for (final AclEntry entry : specific) {
                final Set<AclEntry> conflictingEntries = entries.get(entry.principal());

                if (conflictingEntries == null || conflictingEntries.isEmpty()) {
                    continue;
                }

                final Iterator<AclEntry> conflictingIterator = conflictingEntries.iterator();

                while (conflictingIterator.hasNext()) {
                    final AclEntry conflictingEntry = conflictingIterator.next();

                    for (final AclEntryPermission permission : entry.permissions()) {
                        conflictingEntry.permissions().remove(permission);

                        if (conflictingEntry.permissions().isEmpty()) {
                            conflictingIterator.remove();
                        }
                    }
                }
            }

            try {
                view.setAcl(new ArrayList<>(entries.values()));
            } catch (final IOException e) {
                if (listener != null) {
                    listener.failed(path, e);
                }

                throw e;
            }
        }

        if (recursive && Files.isDirectory(path)) { // NOSONAR NIO API
            try (final Stream<Path> stream = Files.walk(path, 1)) {
                stream.filter(p -> !Files.isSymbolicLink(p)).forEach(p -> {
                    try {
                        setPermissions(p, acl, recursive, listener);
                    } catch (final IOException e) {
                        if (listener == null) {
                            throw new RuntimeException(e); // NOSONAR Used in stream
                        }

                        try {
                            listener.failed(p, e);
                        } catch (final IOException e1) {
                            throw new RuntimeException(e1); // NOSONAR Used in stream
                        }
                    }
                });
            } catch (final RuntimeException e) {
                IOException cause;

                if (e.getCause() instanceof final IOException e2) {
                    cause = e2;
                } else {
                    cause = new IOException(e);
                }

                throw cause;
            }
        }
    }

    /**
     * Sets the permissions without recursive processing.
     * @param path     the path
     * @param acl      the ACL
     * @param listener the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setPermissions(final Path path, final Set<AclEntry> acl, final FileProcessingListener listener) throws IOException {
        setPermissions(path, acl, false, listener);
    }

    /**
     * Sets the POSIX permission.
     * @param entry       the entry
     * @param permission  the permission
     * @param permissions the permissions
     */
    private void setPosixPermission(final AclEntry entry, final PosixFilePermission permission, final Set<PosixFilePermission> permissions) {
        if (AclEntryType.ALLOW.equals(entry.type())) {
            permissions.add(permission);
        } else if (AclEntryType.DENY.equals(entry.type())) {
            permissions.remove(permission);
        }
    }

    /**
     * Sets the POSIX permissions.
     * @param path the path
     * @param acl  the ACL
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void setPosixPermissions(final Path path, final Set<AclEntry> acl) throws IOException {
        final String owner = getOwner(path).getName();
        final GroupPrincipal groupPrincipal = getGroup(path);
        final PosixFileAttributes attributes = Files.readAttributes(path, PosixFileAttributes.class);

        if (attributes == null) {
            return;
        }

        final Set<PosixFilePermission> permissions = new HashSet<>();

        if (attributes.permissions() != null) {
            permissions.addAll(attributes.permissions());
        }

        for (final AclEntry entry : acl) {
            if (entry.principal().getName().equalsIgnoreCase(owner)) {
                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.READ_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OWNER_READ, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.WRITE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OWNER_WRITE, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.EXECUTE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OWNER_EXECUTE, permissions);
                }
            } else if (entry.principal().equals(groupPrincipal)) {
                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.READ_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.GROUP_READ, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.WRITE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.GROUP_WRITE, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.EXECUTE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.GROUP_EXECUTE, permissions);
                }
            } else {
                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.READ_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OTHERS_READ, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.WRITE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OTHERS_WRITE, permissions);
                }

                if (!CollectionUtils.intersection(entry.permissions(), PathUtilities.EXECUTE_ACL_ENTRY_PERMISSIONS).isEmpty()) {
                    setPosixPermission(entry, PosixFilePermission.OTHERS_EXECUTE, permissions);
                }
            }
        }

        Files.setPosixFilePermissions(path, permissions);
    }

    /**
     * Sets the readable.
     * @param path the new readable
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setReadable(final Path path) throws IOException {
        final Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.GROUP_READ);
        Files.setPosixFilePermissions(path, perms);
    }

    /**
     * Sets the writable.
     * @param path the new writable
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setWritable(final Path path) throws IOException {
        final Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.GROUP_WRITE);
        Files.setPosixFilePermissions(path, perms);
    }

    /**
     * Unzip.
     * @param in       the in
     * @param path     the path
     * @param excluded the excluded
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void unzip(final InputStream in, final Path path, final Collection<String> excluded) throws IOException {
        unzip(in, path, excluded, new FileProcessingAdapter());
    }

    /**
     * Unzip.
     * @param in       the input stream
     * @param dir      the directory
     * @param excluded the set of excluded files or directories
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void unzip(final InputStream in, final Path dir, final Collection<String> excluded, final FileProcessingListener listener) throws IOException {
        final Predicate<Path> filter = new FilterPredicate(Collections.emptyList(), excluded, true);

        try (final ZipInputStream zis = new ZipInputStream(new ValidatedZipInputStream(in))) {
            ZipEntry entry = zis.getNextEntry();

            while (entry != null) {
                final Path path = dir.resolve(entry.getName()).toAbsolutePath();

                if (!filter.test(path)) {
                    continue;
                }

                final Path parent = path.getParent();

                if (!Files.exists(parent)) { // NOSONAR NIO API
                    Files.createDirectories(parent);
                }

                if (Files.exists(parent) && Files.isWritable(parent)) { // NOSONAR NIO API
                    if (entry.isDirectory()) {
                        Files.createDirectories(path);
                    } else {
                        Files.copy(zis, path);
                    }
                }

                zis.closeEntry();

                if (listener != null) {
                    listener.processed(path, FileProcessingListener.EXTRACTED);
                }

                entry = zis.getNextEntry();
            }
        }
    }

    /**
     * Unzip.
     * @param file     the file
     * @param dir      the directory
     * @param excluded the set of excluded files or directories
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void unzip(final Path file, final Path dir, final Collection<String> excluded, final FileProcessingListener listener) throws IOException {
        assertReadable(file);
        assertFile(file);

        if (!isValidZipFile(file)) {
            throw new IOException(file.toString() + " is not a valid ZIP file.");
        }

        try (final InputStream in = Files.newInputStream(file)) {
            unzip(in, dir, excluded, listener);
        }
    }

    /**
     * Walk.
     * @param dir      the directory
     * @param filter   the filter
     * @param consumer the consumer
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void walk(final Path dir, final Predicate<Path> filter, final Consumer<Path> consumer) throws IOException {
        try (Stream<Path> pathStream = Files.walk(dir).filter(filter)) {
            pathStream.forEach(consumer::accept);
        }
    }

    /**
     * Write.
     * @param file    the file
     * @param content the content
     * @param charset the charset
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void write(final Path file, final StringBuilder content, final Charset charset) throws IOException {
        final CharsetEncoder encoder = charset.newEncoder();

        try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, encoder))) {
            writer.append(content);
        }
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param out           the output stream
     * @param level         the level from 0 (store) to 9
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final OutputStream out, final byte level, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        zip(path, out, level, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param out           the output stream
     * @param level         the level from 0 (store) to 9
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @param listener      the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final OutputStream out, final byte level, final Collection<String> excluded, final boolean includeHidden, final FileProcessingListener listener) throws IOException {
        assertReadable(path);

        if (Files.isRegularFile(path)) {
            try (ZipOutputStream zos = new ZipOutputStream(out)) {
                zos.setLevel(level);
                final ZipEntry entry = new ZipEntry(path.getFileName().toString());

                if (level <= 0) {
                    entry.setMethod(ZipEntry.STORED);
                } else {
                    entry.setMethod(ZipEntry.DEFLATED);
                }

                try {
                    entry.setSize(Files.size(path));
                    zos.putNextEntry(entry);
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (final IOException e) {
                    if (listener != null) {
                        listener.failed(path, e);
                    }

                    throw e;
                }

                if (listener != null) {
                    listener.processed(path, FileProcessingListener.COMPRESSED);
                }
            }

            return;
        }

        final Predicate<Path> filter = new FilterPredicate(Collections.emptyList(), excluded, includeHidden);

        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            zos.setLevel(level);
            walk(path, filter, p -> {
                if (path.equals(p)) {
                    return;
                }

                final ZipEntry entry = new ZipEntry(path.relativize(p).toString());

                if (level <= 0) {
                    entry.setMethod(ZipEntry.STORED);
                } else {
                    entry.setMethod(ZipEntry.DEFLATED);
                }

                try {
                    if (Files.isRegularFile(p)) {
                        entry.setSize(Files.size(p));
                        zos.putNextEntry(entry);
                        Files.copy(p, zos);
                        zos.closeEntry();
                    }
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
        } catch (final Exception e) {
            if (e.getCause() instanceof final IOException ioe) {
                throw ioe;
            }

            throw new IOException(e);
        }
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param out           the output stream
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final OutputStream out, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        zip(path, out, (byte) 1, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param file          the file
     * @param level         the level from 0 (store) to 9
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @param listener      the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final Path file, final byte level, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        zip(path, file, level, excluded, includeHidden, new FileProcessingAdapter());
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param file          the file
     * @param level         the level from 0 (store) to 9
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @param listener      the listener
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final Path file, final byte level, final Collection<String> excluded, final boolean includeHidden, final FileProcessingListener listener) throws IOException {
        try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE)) {
            zip(path, out, level, excluded, includeHidden, listener);
        }
    }

    /**
     * Zip.
     * @param path          the directory or file
     * @param file          the file
     * @param excluded      the set of excluded files or directories
     * @param includeHidden true to include hidden directories and files
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void zip(final Path path, final Path file, final Collection<String> excluded, final boolean includeHidden) throws IOException {
        zip(path, file, (byte) 1, excluded, includeHidden, new FileProcessingAdapter());
    }
}
