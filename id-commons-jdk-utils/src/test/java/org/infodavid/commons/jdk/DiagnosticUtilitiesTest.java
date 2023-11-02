package org.infodavid.commons.jdk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The Class DiagnosticUtilitiesTest.
 */
@SuppressWarnings("static-method")
class DiagnosticUtilitiesTest extends TestCase {

    /** The resource. */
    private Path resource;

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.test.TestCase#tearDown()
     */
    @AfterEach
    @Override
    public void tearDown() throws Exception {
        if (resource != null) {
            Files.deleteIfExists(resource);
        }

        super.tearDown();
    }

    /**
     * Test build head dump.
     * @throws Exception the exception
     */
    @Test
    void testBuildHeadDump() throws Exception { // NOSONAR No error
        resource = Files.createTempFile(getClass().getSimpleName() + "-heapdump-", ".hprof");
        Files.deleteIfExists(resource);

        DiagnosticUtilities.getInstance().buildHeadDump(resource.toAbsolutePath().toString());

        assertTrue(Files.exists(resource), "File not found");
    }

    /**
     * Test collect diagnostics.
     */
    @Test
    void testCollectDiagnostics() { // NOSONAR No error
        DiagnosticUtilities.getInstance().collectDiagnostics(new StringBuilder());
    }

    /**
     * Test gc.
     * @throws Exception the exception
     */
    @Test
    void testGc() throws Exception { // NOSONAR No error
        DiagnosticUtilities.getInstance().gc();
    }

    /**
     * Test get active live threads.
     */
    @Test
    void testGetActiveLiveThreads() { // NOSONAR No error
        DiagnosticUtilities.getInstance().getActiveLiveThreads(5);
    }

    /**
     * Test get java heap histogram.
     */
    @Test
    void testGetJavaHeapHistogram() { // NOSONAR No error
        DiagnosticUtilities.getInstance().getJavaHeapHistogram("", "");
    }
}
