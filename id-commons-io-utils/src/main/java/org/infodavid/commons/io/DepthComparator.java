package org.infodavid.commons.io;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * The Class DepthComparator.
 */
public class DepthComparator implements Comparator<Path>, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3534719302522258424L;

    /**
     * Instantiates a new comparator.
     */
    public DepthComparator() {
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

        if (path1.getNameCount() == path2.getNameCount()) {
            return 0;
        }

        return path1.getNameCount() > path2.getNameCount() ? -1 : 1;
    }
}
