package org.infodavid.commons.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * The Class FileProcessingAdapter.
 */
public class FileProcessingAdapter implements FileProcessingListener {

    /**
     * Instantiates a new adapter.
     */
    public FileProcessingAdapter() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.io.FileProcessingListener#processed(java.nio.file.Path, byte)
     */
    @Override
    public void processed(final Path path, final byte action) {
        Objects.requireNonNull(path);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.io.FileProcessingListener#failed(java.nio.file.Path, java.io.IOException)
     */
    @Override
    public void failed(final Path path, final IOException e) {
        Objects.requireNonNull(path);
    }
}
