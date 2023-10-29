package org.infodavid.commons.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * The Class NamedModelObject.
 * @param <K> the key type
 */
@MappedSuperclass
public class NamedModelObject<K extends Serializable> extends AbstractModelObject<K> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4523886689208621786L;

    /** The name. */
    @Column(name = "name", length = 48, nullable = true)
    private String name;

    /**
     * Instantiates a new named persistent object.
     */
    protected NamedModelObject() {
    }

    /**
     * Instantiates a new named persistent object.
     * @param source the source
     */
    protected NamedModelObject(final NamedModelObject<K> source) {
        super(source);
        name = source.name;
    }

    /**
     * Instantiates a new named persistent object.
     * @param id the identifier
     */
    protected NamedModelObject(final K id) {
        super(id);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.AbstractModelObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof final NamedModelObject<?> castOther)) {
            return false;
        }

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
     * @see org.infodavid.commons.model.AbstractModelObject#hashCode()
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
