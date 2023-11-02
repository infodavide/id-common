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
        super(SHA512ChecksumGenerator.ALGORITHM, "99363ee47e248349f5c3009ee04ab8343b27651e1f493e38007320bf7cf450c56b6ec28ea3d6e80c1396c76127200b4cf07688502e519979f6212b01db8196dd");
    }
}
