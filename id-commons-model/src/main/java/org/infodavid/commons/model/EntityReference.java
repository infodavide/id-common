package org.infodavid.commons.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class EntityReference.
 */
public class EntityReference extends AbstractObject<Serializable> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -449021796863253704L;

    /**
     * Returns the value associated to the given entity.
     * @param value the value
     * @return the default entity reference
     */
    public static EntityReference valueOf(final NamedObject<Long> value) {
        return new EntityReference(value.getId(), value.getName());
    }

    /** The label. */
    private String label;

    /**
     * Instantiates a new entity reference.
     */
    public EntityReference() {
    }

    /**
     * Instantiates a new entity reference.
     * @param source the source
     */
    public EntityReference(final EntityReference source) {
        super(source);
        label = source.label;
    }

    /**
     * Instantiates a new entity reference.
     * @param id    the identifier
     * @param label the label
     */
    public EntityReference(final Long id, final String label) {
        super(id);
        this.label = label;
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

        if (!(obj instanceof EntityReference)) {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * Gets the label.
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Sets the label.
     * @param label the label to set
     */
    public void setLabel(final String label) {
        this.label = label;
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
