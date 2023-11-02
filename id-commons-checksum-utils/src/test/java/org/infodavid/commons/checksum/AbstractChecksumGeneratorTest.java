package org.infodavid.commons.checksum;

import static org.infodavid.commons.test.Assertions.assertEquals;
import static org.infodavid.commons.test.Assertions.assertThrows;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * The Class AbstractChecksumGeneratorTest.
 */
public abstract class AbstractChecksumGeneratorTest extends TestCase {

    /**
     * Gets the test path.
     * @return the test path
     * @throws URISyntaxException the URI syntax exception
     */
    protected static Path getTestPath() throws URISyntaxException {
        return Paths.get(Thread.currentThread().getContextClassLoader().getResource("test.png").toURI());
    }

    /** The algorithm. */
    private final String algorithm;

    /** The expected checksum. */
    private final String expectedChecksum;

    /** The generator. */
    private final ChecksumGenerator generator;

    /**
     * Instantiates a new abstract checksum generator test.
     * @param algorithm        the algorithm
     * @param expectedChecksum the expected checksum
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    protected AbstractChecksumGeneratorTest(final String algorithm, final String expectedChecksum) throws NoSuchAlgorithmException {
        this.algorithm = algorithm;
        this.expectedChecksum = expectedChecksum;
        generator = ChecksumGeneratorRegistry.getInstance().getGenerator(algorithm);
    }

    /**
     * Test get algorithm.
     * @throws Exception the exception
     */
    @Test
    public void testGetAlgorithm() throws Exception {
        assertEquals("Unexpected algorithm", algorithm, generator.getAlgorithm());
    }

    /**
     * Test get checksum.
     * @throws Exception the exception
     */
    @Test
    public void testGetChecksumOnContent() throws Exception {
        final String computed = generator.getChecksum(Files.readAllBytes(getTestPath()));

        System.out.println(expectedChecksum);
        System.out.println(computed);
        assertEquals("Wrong checksum", expectedChecksum, computed);
    }

    /**
     * Test get checksum null.
     * @throws Exception the exception
     */
    @Test
    public void testGetChecksumOnContentNull() throws Exception {
        assertThrows("Exception not raised or has a wrong type", IllegalArgumentException.class, () -> generator.getChecksum((byte[])null));
    }

    /**
     * Test get checksum.
     * @throws Exception the exception
     */
    @Test
    public void testGetChecksumOnFile() throws Exception {
        final String computed = generator.getChecksum(getTestPath());

        System.out.println(expectedChecksum);
        System.out.println(computed);
        assertEquals("Wrong checksum", expectedChecksum, computed);
    }

    /**
     * Test get checksum null.
     * @throws Exception the exception
     */
    @Test
    public void testGetChecksumOnFileNull() throws Exception {
        assertThrows("Exception not raised or has a wrong type", IllegalArgumentException.class, () -> generator.getChecksum((Path) null));
    }

    /**
     * Test get checksum missing.
     * @throws Exception the exception
     */
    @Test
    public void testGetChecksumOnMissingFile() throws Exception {
        final File file = new File("target/test-classes/missing_image.png");

        assertThrows("Exception not raised or has a wrong type", NoSuchFileException.class, () -> generator.getChecksum(file.toPath()));
    }
}
