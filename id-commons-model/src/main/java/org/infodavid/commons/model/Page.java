package org.infodavid.commons.model;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class Page.
 * @param <T> the generic type
 */
public class Page<T> {

    /** The number. */
    private int number;

    /** The results. */
    private List<T> results;

    /** The size. */
    private int size;

    /** The total size. */
    private long totalSize;

    /**
     * Gets the page number.
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the results.
     * @return the results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Gets the size.
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the total size.
     * @return the totalSize
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * Sets the number.
     * @param number the page number to set
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Sets the results.
     * @param results the results to set
     */
    public void setResults(final List<T> results) {
        this.results = results;
    }

    /**
     * Sets the size.
     * @param size the size to set
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * Sets the total size.
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(final long totalSize) {
        this.totalSize = totalSize;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
