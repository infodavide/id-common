package org.infodavid.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.infodavid.commons.test.TestCase;
import org.infodavid.commons.utility.ProcessingDuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * The Class FileCacheTest.
 */
class FileCacheTest extends TestCase {

    /** The cache. */
    private final FileCache cache = new FileCache();

    /** The resource. */
    private Path resource;

    /** The resource length. */
    private long resourceLength;

    /**
     * Sets the up.
     * @throws Exception the exception
     */
    @Override
    @BeforeEach
    public void setUp(final TestInfo info) throws Exception {
        super.setUp(info);
        cache.invalidate();
        resource = Files.createTempFile(getClass().getSimpleName() + '-', ".tmp");

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png")) {
            Files.copy(in, resource, StandardCopyOption.REPLACE_EXISTING);
        }

        resourceLength = Files.size(resource);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.test.TestCase#tearDown()
     */
    @AfterEach
    @Override
    public void tearDown() throws Exception {
        Files.deleteIfExists(resource);
        super.tearDown();
    }

    /**
     * Test get data using path.
     * @throws Exception the exception
     */
    @Test
    void testGetDataUsingPath() throws Exception {
        final ProcessingDuration<byte[]> counter = new ProcessingDuration<>(() -> cache.getData(resource));
        final byte[] result1 = counter.run();
        final long duration1 = counter.getDuration();

        System.out.println("duration1: " + duration1); // NOSONAR

        assertNotNull(result1, "Result is null");
        assertEquals(resourceLength, result1.length, "Result length is wrong");
        assertEquals(1, cache.getSize(), "Cache size is wrong");

        final byte[] result2 = counter.run();
        final long duration2 = counter.getDuration();

        System.out.println("duration2: " + duration2); // NOSONAR

        assertNotNull(result2, "Result is null");
        assertEquals(result1.length, result2.length, "Result length is wrong");
        assertArrayEquals(result1, result2, "Wrong data");
        assertEquals(1, cache.getSize(), "Cache size is wrong");
    }

    /**
     * Test get data using URI.
     * @throws Exception the exception
     */
    @Test
    void testReloadModifiedDataUsingPath() throws Exception {
        final ProcessingDuration<byte[]> counter = new ProcessingDuration<>(() -> cache.getData(resource));
        final byte[] result1 = counter.run();
        final long duration1 = counter.getDuration();
        System.out.println("duration1: " + duration1); // NOSONAR

        assertNotNull(result1, "Result is null");
        assertEquals(resourceLength, result1.length, "Result length is wrong");
        assertEquals(1, cache.getSize(), "Cache size is wrong");

        sleep(250);
        final String text = String.valueOf(System.currentTimeMillis());
        Files.writeString(resource, text, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.SYNC);
        sleep(250);

        final byte[] result2 = counter.run();
        final long duration2 = counter.getDuration();

        System.out.println("duration2: " + duration2); // NOSONAR

        assertNotNull(result2, "Result is null");
        assertEquals(text.length(), result2.length, "Result length is wrong");
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), result2, "Wrong data");
        assertEquals(1, cache.getSize(), "Cache size is wrong");
    }
}
