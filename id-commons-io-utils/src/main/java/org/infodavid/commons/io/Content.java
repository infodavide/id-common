package org.infodavid.commons.io;

/**
 * The Class Content.
 */
class Content {

    /** The content. */
    private final byte[] data;

    /** The modification date. */
    private final long modificationDate;

    /**
     * Instantiates a new content.
     * @param data the data
     * @param modificationDate the modification date
     */
    public Content(final byte[] data, final long modificationDate) {
        this.data = data;
        this.modificationDate = modificationDate;
    }

    /**
     * Gets the data.
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Gets the modification date.
     * @return the modification date
     */
    public long getModificationDate() {
        return modificationDate;
    }
}
