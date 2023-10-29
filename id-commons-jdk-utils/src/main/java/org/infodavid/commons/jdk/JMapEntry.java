package org.infodavid.commons.jdk;
import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class JMapEntry.
 */
public class JMapEntry implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3892407845521916529L;

    /** The bytes. */
    private long bytes = 0;

    /** The class name. */
    private String className;

    /** The instances. */
    private long instances = 0;

    /**
     * Instantiates a new entry.
     * @param className the class name
     */
    public JMapEntry(final String className) {
        this.className = className;
    }

    /**
     * Instantiates a new entry.
     * @param className the class name
     * @param instances the instances
     * @param bytes the bytes
     */
    public JMapEntry(final String className, final long instances, final long bytes) {
        this.className = className;
        this.instances = instances;
        this.bytes = bytes;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final JMapEntry other = (JMapEntry)obj;

        return Objects.equals(className, other.className);
    }

    /**
     * Gets the bytes.
     * @return the bytes
     */
    public long getBytes() {
        return bytes;
    }

    /**
     * Gets the class name.
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the instances.
     * @return the instances
     */
    public long getInstances() {
        return instances;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    /**
     * Sets the bytes.
     * @param bytes the bytes to set
     */
    public void setBytes(final long bytes) {
        this.bytes = bytes;
    }

    /**
     * Sets the class name.
     * @param className the className to set
     */
    public void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Sets the instances.
     * @param instances the instances to set
     */
    public void setInstances(final long instances) {
        this.instances = instances;
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
