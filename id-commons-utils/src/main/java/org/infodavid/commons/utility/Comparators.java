package org.infodavid.commons.utility;

import java.util.Comparator;

/**
 * The Class Comparators.
 */
public class Comparators {

    /**
     * The Class StringLengthComparator.
     */
    public static class StringLengthComparator implements Comparator<String> {

        /**
         * Instantiates a new comparator.
         */
        public StringLengthComparator() {
            // noop
        }

        /*
         * (non-javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final String o1, final String o2) {
            if (o1 == o2) { //NOSONAR Compare references
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }

            if (o1.length() != o2.length()) {
                return o1.length() - o2.length();
            }

            return o1.compareTo(o2);
        }
    }

    /**
     * The Class ClassNameComparator.
     */
    @SuppressWarnings("rawtypes")
    public static class ClassNameComparator implements Comparator<Class> {

        /**
         * Instantiates a new comparator.
         */
        public ClassNameComparator() {
            // noop
        }

        /*
         * (non-javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final Class o1, final Class o2) {
            if (o1 == o2) { //NOSONAR Compare references
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }

            return o1.getName().compareTo(o2.getName());
        }
    }

    /**
     * The Class HashCodeComparator.
     * @param <T> the generic type
     */
    public static class HashCodeComparator<T> implements Comparator<T> {

        /**
         * Instantiates a new comparator.
         */
        public HashCodeComparator() {
            // noop
        }

        /*
         * (non-javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final T o1, final T o2) {
            if (o1 == o2) { //NOSONAR Compare references
                return 0;
            }
            if (o1 == null) {
                return -1;
            }
            if (o2 == null) {
                return 1;
            }

            return Integer.compare(o1.hashCode(), o2.hashCode());
        }
    }

    /**
     * Instantiates a new comparators.
     */
    private Comparators() {
    }
}
