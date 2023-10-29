package org.infodavid.commons.checksum;

import java.security.NoSuchAlgorithmException;

/**
 * The Class SHA512ChecksumGeneratorTest.
 */
public class SHA512ChecksumGeneratorTest extends AbstractChecksumGeneratorTest {

    /**
     * Instantiates a new SHA 512 checksum generator test.
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public SHA512ChecksumGeneratorTest() throws NoSuchAlgorithmException {
        super(SHA512ChecksumGenerator.ALGORITHM, "aa4e5ab73173576f808b25e1ea8a77fa700bca7d6faf6f2aeca25b3f4aecc4a534ee414e9ae5ab991e3a12c144543de74e421a8cf604fb35c8bc7dcda7848e7e");
    }
}
