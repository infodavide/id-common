package org.infodavid.commons.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;

/**
 * The Class NamedObject.
 * @param <K> the key type
 */
public class NamedObject<K extends Serializable> extends AbstractObject<K> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4523886689208621786L;

    /** The name. */
    @Column(name = "name", length = 48)
    private String name;

    /**
     * Instantiates a new named persistent object.
     */
    protected NamedObject() {
    }

    /**
     * Instantiates a new named persistent object.
     * @param source the source
     */
    protected NamedObject(final NamedObject<K> source) {
        super(source);
        name = source.name;
    }

    /**
     * Instantiates a new named persistent object.
     * @param id the identifier
     */
    protected NamedObject(final K id) {
        super(id);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.AbstractObject#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof NamedObject)) {
            return false;
        }

        final NamedObject castOther = (NamedObject)other;

        return new EqualsBuilder().append(name, castOther.getName()).isEquals();
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.AbstractObject#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }

    /**
     * Sets the name.
     * @param value the new name
     */
    public void setName(final String value) {
        name = value;
    }
}
