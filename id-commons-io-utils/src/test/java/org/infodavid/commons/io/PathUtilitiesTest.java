package org.infodavid.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import net.lingala.zip4j.ZipFile;

/**
 * The Class PathUtilitiesTest.
 */
@SuppressWarnings("static-method")
class PathUtilitiesTest extends TestCase {

    /** The resource. */
    private Path resource;

    /** The resource directory. */
    private Path resourceDirectory;

    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        resourceDirectory = Files.createTempDirectory(getClass().getSimpleName());
        resource = resourceDirectory.resolve("test.png");

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png")) {
            Files.copy(in, resource, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.test.TestCase#tearDown()
     */
    @AfterEach
    @Override
    public void tearDown() throws Exception {
        Files.deleteIfExists(resource);
        Files.deleteIfExists(resourceDirectory);
        super.tearDown();
    }

    /**
     * Test get group.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testGetGroup() throws Exception {
        final UserPrincipal group = PathUtilities.getInstance().getGroup(resource);

        System.out.println(group);
        assertNotNull(group, "Null group");
    }

    /**
     * Test get group with wrong path.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testGetGroupWithWrongPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "wrong.txt");

        assertThrows(NoSuchFileException.class, () -> PathUtilities.getInstance().getGroup(file.toPath()), "Exception not raised or has a wrong type");
    }

    /**
     * Test get owner.
     * @throws Exception the exception
     */
    @Test
    void testGetOwner() throws Exception {
        final UserPrincipal owner = PathUtilities.getInstance().getOwner(resource);

        System.out.println(owner);
        assertNotNull(owner, "Null owner");
    }

    /**
     * Test get owner with wrong path.
     * @throws Exception the exception
     */
    @Test
    void testGetOwnerWithWrongPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "wrong.txt");

        assertThrows(NoSuchFileException.class, () -> PathUtilities.getInstance().getOwner(file.toPath()), "Exception not raised or has a wrong type");
    }

    /**
     * Test get permissions.
     * @throws Exception the exception
     */
    @Test
    void testGetPermissions() throws Exception {
        final Set<AclEntry> permissions = PathUtilities.getInstance().getPermissions(resource);

        System.out.println(permissions);
        assertNotNull(permissions, "Null permissions");
        assertFalse(permissions.isEmpty(), "Empty permissions");
    }

    /**
     * Test get permissions with wrong path.
     * @throws Exception the exception
     */
    @Test
    void testGetPermissionsWithWrongPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "wrong.txt");

        assertThrows(NoSuchFileException.class, () -> PathUtilities.getInstance().getPermissions(file.toPath()), "Exception not raised or has a wrong type");
    }

    /**
     * Test set owner.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testSetOwner() throws Exception {
        final UserPrincipal owner = PathUtilities.getInstance().getOwner(resource);
        final GroupPrincipal group = PathUtilities.getInstance().getGroup(resource);
        System.out.println(owner);

        PathUtilities.getInstance().setOwner(resource, owner.getName(), group == null ? null : group.getName());

        assertEquals(owner, PathUtilities.getInstance().getOwner(resource), "Wrong owner");
    }

    /**
     * Test set owner with wrong path.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testSetOwnerWithWrongPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "wrong.txt");

        assertThrows(NoSuchFileException.class, () -> PathUtilities.getInstance().setOwner(file.toPath(), null, null), "Exception not raised or has a wrong type");
    }

    /**
     * Test set permissions.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testSetPermissions() throws Exception {
        final UserPrincipal owner = PathUtilities.getInstance().getOwner(resource);
        Set<AclEntry> permissions = new HashSet<>();
        permissions.add(AclEntry.newBuilder().setType(AclEntryType.ALLOW).setPrincipal(owner).setPermissions(AclEntryPermission.READ_DATA).build());
        permissions.add(AclEntry.newBuilder().setType(AclEntryType.DENY).setPrincipal(owner).setPermissions(AclEntryPermission.WRITE_DATA).build());

        PathUtilities.getInstance().setPermissions(resource, permissions);

        permissions = PathUtilities.getInstance().getPermissions(resource);
        System.out.println(permissions);
        assertNotNull(permissions, "Null permissions");
        assertFalse(permissions.isEmpty(), "Empty permissions");
        assertTrue(Files.isReadable(resource), "Wrong permission");
        assertFalse(Files.isWritable(resource), "Wrong permission");
    }

    /**
     * Test set permissions with wrong path.
     * @throws Exception the exception
     */
    @EnabledOnOs(OS.LINUX)
    @Test
    void testSetPermissionsWithWrongPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "wrong.txt");

        assertThrows(NoSuchFileException.class, () -> PathUtilities.getInstance().setPermissions(file.toPath(), new HashSet<>()), "Exception not raised or has a wrong type");
    }

    /**
     * Test unzip.
     * @throws Exception the exception
     */
    @Test
    void testUnzip() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), getClass().getSimpleName() + ".zip");
        final File extractedDir = new File(FileUtils.getTempDirectory(), "extracted_" + getClass().getSimpleName());
        final File sourceDir = resourceDirectory.toFile();

        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }

        if (extractedDir.exists()) {
            FileUtils.deleteQuietly(extractedDir);
        }

        try (final ZipFile zip = new ZipFile(file)) {

            for (final File f : sourceDir.listFiles()) {
                if (f.isDirectory()) {
                    zip.addFolder(f);
                } else if (f.isFile() && !f.isHidden() && !f.getName().startsWith(".")) {
                    zip.addFile(f);
                }
            }
        }

        try (InputStream in = new FileInputStream(file)) {
            PathUtilities.getInstance().unzip(in, extractedDir.toPath(), Collections.emptySet());
        }

        assertTrue(extractedDir.exists(), "Directory not created");

        try {
            System.out.println(PathUtilities.getInstance().printDirectoryTree(sourceDir.toPath()));
            System.out.println(PathUtilities.getInstance().printDirectoryTree(extractedDir.toPath(), true));
            assertTrue(PathUtilities.getInstance().compare(sourceDir.toPath(), extractedDir.toPath()), "Directory content is wrong");
        } finally {
            FileUtils.deleteQuietly(file);
        }
    }

    /**
     * Test unzip with invalid file.
     * @throws Exception the exception
     */
    @Test
    void testUnzipWithInvalidFile() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), getClass().getSimpleName() + ".zip");
        final File extractedDir = new File(FileUtils.getTempDirectory(), "extracted_" + getClass().getSimpleName());

        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }

        FileUtils.copyFile(resource.toFile(), file);

        assertThrows(IOException.class, () -> { // NOSONAR No lambda
            try (InputStream in = new FileInputStream(file)) {
                PathUtilities.getInstance().unzip(in, extractedDir.toPath(), Collections.emptySet());
            } finally {
                FileUtils.deleteQuietly(file);
            }
        }, "Exception not raised or has a wrong type");
    }

    /**
     * Test zip.
     * @throws Exception the exception
     */
    @Test
    void testZip() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), getClass().getSimpleName() + ".zip");
        final File extractedDir = new File(FileUtils.getTempDirectory(), "extracted_" + getClass().getSimpleName());
        final File sourceDir = resourceDirectory.toFile();

        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }

        if (extractedDir.exists()) {
            FileUtils.deleteQuietly(extractedDir);
        }

        try (OutputStream out = new FileOutputStream(file)) {
            PathUtilities.getInstance().zip(sourceDir.toPath(), out, Collections.emptySet(), true);
        }

        assertTrue(file.exists(), "File not created");

        try {
            assertTrue(file.length() > 0, "Created file is empty");

            try (final ZipFile zip = new ZipFile(file)) {
                zip.extractAll(extractedDir.getAbsolutePath());
            }

            System.out.println(PathUtilities.getInstance().printDirectoryTree(sourceDir.toPath()));
            System.out.println(PathUtilities.getInstance().printDirectoryTree(extractedDir.toPath(), true));
            assertTrue(PathUtilities.getInstance().compare(sourceDir.toPath(), extractedDir.toPath()), "Zip is malformed");
        } finally {
            FileUtils.deleteQuietly(file);
            FileUtils.deleteQuietly(extractedDir);
        }
    }

    /**
     * Test zip with invalid path.
     * @throws Exception the exception
     */
    @Test
    void testZipWithInvalidPath() throws Exception {
        final File file = new File(FileUtils.getTempDirectory(), "p1.zip");

        if (file.exists()) {
            FileUtils.deleteQuietly(file);
        }

        assertThrows(NoSuchFileException.class, () -> { // NOSONAR No lambda
            try (OutputStream out = new FileOutputStream(file)) {
                PathUtilities.getInstance().zip(new File("invalid").toPath(), out, Collections.emptySet(), false);
            }
        }, "Exception not raised or has a wrong type");
        assertTrue(file.exists(), "File not created");
        FileUtils.deleteQuietly(file);
    }
}
