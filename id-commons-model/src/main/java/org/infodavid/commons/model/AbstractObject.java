package org.infodavid.commons.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.annotation.processing.Generated;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.infodavid.commons.model.annotation.ModelObject;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * The Class AbstractObject.
 * @param <K> the key type
 */
@ModelObject
public abstract class AbstractObject<K extends Serializable> implements PersistentObject<K> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7292704857170542767L;

    /** The creation date. */
    @Column(name = "creation_date")
    private Date creationDate;

    /** The identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private K id;

    /** The modification date. */
    @Column(name = "modification_date")
    private Date modificationDate;

    /**
     * The Constructor.
     */
    protected AbstractObject() {
    }

    /**
     * The Constructor.
     * @param source the source
     */
    protected AbstractObject(final AbstractObject<K> source) {
        creationDate = source.creationDate;
        id = source.id;
        modificationDate = source.modificationDate;
    }

    /**
     * The Constructor.
     * @param id the identifier
     */
    protected AbstractObject(final K id) {
        this.id = id;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof PersistentObject)) {
            return false;
        }

        final PersistentObject other = (PersistentObject) obj;

        return Objects.equals(id, other.getId());
    }

    /**
     * See super class or interface. (non-Javadoc)
     * @return the creation date
     */
    @Override
    @Generated("Generated when inserting the data into the database")
    public Date getCreationDate() {
        return creationDate;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#getId()
     */
    @Override
    public K getId() {
        return id;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#getModificationDate()
     */
    @Override
    @Generated("Generated when updating the data into the database")
    public Date getModificationDate() {
        return modificationDate;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#setCreationDate(java.util.Date)
     */
    @Override
    public void setCreationDate(final Date value) {
        creationDate = value;

        if (getModificationDate() == null) {
            setModificationDate(value);
        }
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#setId(java.io.Serializable)
     */
    @Override
    public void setId(final K value) {
        id = value;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#setModificationDate(java.util.Date)
     */
    @Override
    public void setModificationDate(final Date value) {
        modificationDate = value;
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
