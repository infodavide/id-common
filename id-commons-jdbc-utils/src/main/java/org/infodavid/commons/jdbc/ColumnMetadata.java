package org.infodavid.commons.jdbc;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class ColumnMetadata.
 */
public class ColumnMetadata implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4299902273975842124L;

    /** The display size. */
    private int displaySize;

    /** The index. */
    private int index;

    /** The label. */
    private String label;

    /** The name. */
    private String name;

    /** The type. */
    private int type;

    /** The type name. */
    private String typeName;

    /**
     * Instantiates a new column metadata.
     */
    public ColumnMetadata() {
        // noop
    }

    /*
     * (non-Javadoc)
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

        if (!(obj instanceof final ColumnMetadata other)) {
            return false;
        }

        if (index != other.index) {
            return false;
        }

        if (!Objects.equals(name, other.name)) {
            return false;
        }

        return type == other.type;
    }

    /**
     * Gets the display size.
     * @return the displaySize
     */
    public int getDisplaySize() {
        return displaySize;
    }

    /**
     * Gets the index.
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the label.
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type.
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the type name.
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(index, name, type);
    }

    /**
     * Sets the display size.
     * @param displaySize the displaySize to set
     */
    public void setDisplaySize(final int displaySize) {
        this.displaySize = displaySize;
    }

    /**
     * Sets the index.
     * @param index the index to set
     */
    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Sets the label.
     * @param label the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the type.
     * @param type the type to set
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * Sets the type name.
     * @param typeName the typeName to set
     */
    public void setTypeName(final String typeName) {
        this.typeName = typeName;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
