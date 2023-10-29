package org.infodavid.commons.jdk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * The Class DiagnosticUtilitiesTest.
 */
@SuppressWarnings("static-method")
class DiagnosticUtilitiesTest extends TestCase {

    /**
     * Test build head dump.
     * @throws Exception the exception
     */
    @Test
    void testBuildHeadDump() throws Exception { // NOSONAR No error
        final File path = new File("target/heapdump.hprof");

        if (path.exists()) {
            path.delete();

        }

        DiagnosticUtilities.getInstance().buildHeadDump(path.getAbsolutePath());

        assertTrue(path.exists(), "File not found");
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
