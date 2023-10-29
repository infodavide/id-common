package org.infodavid.commons.checksum;

import java.security.NoSuchAlgorithmException;

/**
 * The Class MD5ChecksumGeneratorTest.
 */
public class MD5ChecksumGeneratorTest extends AbstractChecksumGeneratorTest {

    /**
     * Instantiates a new MD 5 checksum generator test.
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public MD5ChecksumGeneratorTest() throws NoSuchAlgorithmException {
        super(MD5ChecksumGenerator.ALGORITHM, "75d4fe0b1462895b5eead7f8823340e7");
    }
}
