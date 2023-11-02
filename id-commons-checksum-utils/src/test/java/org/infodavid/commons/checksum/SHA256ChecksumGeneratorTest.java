package org.infodavid.commons.checksum;

import java.security.NoSuchAlgorithmException;

/**
 * The Class SHA256ChecksumGeneratorTest.
 */
public class SHA256ChecksumGeneratorTest extends AbstractChecksumGeneratorTest {

    /**
     * Instantiates a new SHA 256 checksum generator test.
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public SHA256ChecksumGeneratorTest() throws NoSuchAlgorithmException {
        super(SHA256ChecksumGenerator.ALGORITHM, "a4c19a597c4279fb9f666e6a87bd439141080d8ba90f7c77c25e0f8621a53f1b");
    }
}
