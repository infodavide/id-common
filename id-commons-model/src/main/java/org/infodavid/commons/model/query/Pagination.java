package org.infodavid.commons.model.query;

import java.util.Collections;
import java.util.List;

/**
 * The Class Pagination.
 */
public class Pagination {

    /** The Constant UNUSED. */
    public static final Pagination UNUSED = new Pagination(1, -1, Collections.emptyList());

    /** The number. */
    private int number;

    /** The size. */
    private int size;

    /** The sort. */
    private List<String> sort;

    /**
     * Instantiates a new pagination parameters.
     */
    public Pagination() {
        // noop
    }

    /**
     * Instantiates a new pagination parameters.
     * @param number the number
     * @param size   the size
     * @param sort   the sort
     */
    public Pagination(final int number, final int size, final List<String> sort) {
        this.number = number;
        this.size = size;
        this.sort = sort;
    }

    /**
     * Gets the number.
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the size.
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the sort.
     * @return the sort
     */
    public List<String> getSort() {
        return sort;
    }

    /**
     * Sets the number.
     * @param number the new number
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Sets the size.
     * @param size the new size
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * Sets the sort.
     * @param sort the new sort
     */
    public void setSort(final List<String> sort) {
        this.sort = sort;
    }
}
