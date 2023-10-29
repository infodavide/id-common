package org.infodavid.commons.utility;

import java.util.Comparator;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class PropertiesComparator.
 */
public class PropertiesComparator implements Comparator<Properties> {

    /** The properties. */
    private final String[] properties;

    /**
     * Instantiates a new properties comparator.
     * @param properties the properties
     */
    public PropertiesComparator(final String[] properties) {
        this.properties = properties;
    }

    /*
     * (non-javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Properties o1, final Properties o2) {
        if (properties == null || properties.length == 0) {
            return 0;
        }

        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }

        for (final String property : properties) {
            final int result = StringUtils.compare(o1.getProperty(property), o2.getProperty(property));

            if (result != 0) {
                return result;
            }
        }

        return 0;
    }
}
