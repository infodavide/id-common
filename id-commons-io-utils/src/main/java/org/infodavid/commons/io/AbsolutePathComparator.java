package org.infodavid.commons.io;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class AbsolutePathComparator.
 */
public class AbsolutePathComparator implements Comparator<Path>, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3334719302522258424L;

    /**
     * Instantiates a new comparator.
     */
    public AbsolutePathComparator() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Path path1, final Path path2) {
        if (path1 == path2) {
            return 0;
        }

        if (path1 == null) {
            return 1;
        }

        if (path2 == null) {
            return -1;
        }

        return StringUtils.compareIgnoreCase(path1.toAbsolutePath().toString(), path2.toAbsolutePath().toString());
    }
}