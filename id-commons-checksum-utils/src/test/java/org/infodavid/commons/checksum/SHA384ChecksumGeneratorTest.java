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
        super(SHA384ChecksumGenerator.ALGORITHM, "7df37820d3ce4f409b2b9acf69643afcdb45e91cae13b5f6c8349f809a160b7843a4063d545fde212a7cd0da15afe3b4");
    }
}
