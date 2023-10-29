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
        super(SHA256ChecksumGenerator.ALGORITHM, "843bad8051f411621413735f55f59eb8fc1bea676cd9ec546bbec0dcf284a260");
    }
}
