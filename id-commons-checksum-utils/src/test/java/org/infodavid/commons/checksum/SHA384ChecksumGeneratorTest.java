package org.infodavid.commons.checksum;

import java.security.NoSuchAlgorithmException;

/**
 * The Class SHA384ChecksumGeneratorTest.
 */
public class SHA384ChecksumGeneratorTest extends AbstractChecksumGeneratorTest {

    /**
     * Instantiates a new SHA 384 checksum generator test.
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public SHA384ChecksumGeneratorTest() throws NoSuchAlgorithmException {
        super(SHA384ChecksumGenerator.ALGORITHM, "0d35969252812b63d22a0fc28e281693f7e4659ee2cf404fb5651dc289ee8a2d50cc7f99f3b5eab31d088da55aa00956");
    }
}
