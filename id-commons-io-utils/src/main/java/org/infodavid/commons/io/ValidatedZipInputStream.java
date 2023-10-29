package org.infodavid.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.apache.commons.lang3.ArrayUtils;
import org.infodavid.commons.utility.NumberUtilities;

/**
 * The Class ValidatedZipInputStream.
 */
class ValidatedZipInputStream extends FilterInputStream {

    /** The Constant ZIP_SIGNATURE. */
    static final int ZIP_SIGNATURE = 0x504B0304;

    /** The Constant EMPTY_ZIP_SIGNATURE. */
    static final int EMPTY_ZIP_SIGNATURE = 0x504B0506;

    /** The Constant SPANNED_ZIP_SIGNATURE. */
    static final int SPANNED_ZIP_SIGNATURE = 0x504B0708;

    /**
     * Instantiates a new input stream.
     * @param proxy the proxy
     */
    public ValidatedZipInputStream(final InputStream proxy) {
        super(proxy);
    }

    /** The signature. */
    private byte[] signature = new byte[0];

    /** The read limit. */
    private int readlimit;

    /**
     * Gets the signature.
     * @return the signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Invokes the delegate's <code>read()</code> method.
     * @return the byte read or -1 if the end of stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        final int result = in.read();

        if (signature.length < 4 && result >= 0) {
            signature = ArrayUtils.add(signature, (byte)result);

            validate();
        }

        return result;
    }

    /*
     * (non-javadoc)
     * @see java.io.FilterInputStream#mark(int)
     */
    @Override
    public synchronized void mark(final int readlimit) {
        super.mark(readlimit);
        this.readlimit = readlimit;
    }

    /*
     * (non-javadoc)
     * @see java.io.FilterInputStream#reset()
     */
    @Override
    public synchronized void reset() throws IOException {
        if (readlimit >= 0 && readlimit < 4) {
            signature = ArrayUtils.subarray(signature, 0, readlimit);
        }

        super.reset();
    }

    /**
     * Validate.
     * @throws ZipException the zip exception
     */
    private void validate() throws ZipException {
        if (signature.length == 4) {
            final int integer = NumberUtilities.getInstance().toInt(signature);

            if (integer != ZIP_SIGNATURE && integer != EMPTY_ZIP_SIGNATURE && integer != SPANNED_ZIP_SIGNATURE) {
                throw new ZipException("Not a valid zip content");
            }
        }
    }

    /**
     * Invokes the delegate's <code>read(byte[])</code> method.
     * @param buffer the buffer to read the bytes into
     * @return the number of bytes read or EOF if the end of stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(final byte[] buffer) throws IOException {
        final int result = in.read(buffer);

        if (signature.length < 4 && result > 0) {
            final int remaining = 4 - signature.length;

            if (result > remaining) {
                signature = ArrayUtils.addAll(signature, ArrayUtils.subarray(buffer, 0, remaining));
            }
            else {
                signature = ArrayUtils.addAll(signature, ArrayUtils.subarray(buffer, 0, result));
            }

            validate();
        }

        return result;
    }

    /**
     * Invokes the delegate's <code>read(byte[], int, int)</code> method.
     * @param buffer the buffer to read the bytes into
     * @param off The start offset
     * @param len The number of bytes to read
     * @return the number of bytes read or -1 if the end of stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(final byte[] buffer, final int off, final int len) throws IOException {
        final int result = in.read(buffer, off, len);

        if (signature.length < 4 && result > 0) {
            final int remaining = 4 - signature.length;

            if (result > remaining) {
                signature = ArrayUtils.addAll(signature, ArrayUtils.subarray(buffer, off, off + remaining));
            }
            else {
                signature = ArrayUtils.addAll(signature, ArrayUtils.subarray(buffer, off, off + result));
            }

            validate();
        }

        return result;
    }
}
