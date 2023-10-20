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
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

/**
 * The Class AbstractObject.
 * @param <K> the key type
 */
@ModelObject
@MappedSuperclass
public abstract class AbstractObject<K extends Serializable> implements PersistentObject<K> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7292704857170542767L;

    /** The creation date. */
    @Column(name = "creation_date", columnDefinition = "datetime default NOW()", updatable = false, nullable = false)
    private Date creationDate;

    /** The identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private K id;

    /** The modification date. */
    @Column(name = "modification_date", columnDefinition = "datetime default NOW()", nullable = false)
    @Version
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

    /**
     * Equals.
     * @param obj the object
     * @return true, if successful
     */
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

        if (!(obj instanceof final PersistentObject<?> other)) {
            return false;
        }

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

    /**
     * Gets the id.
     * @return the id
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#getId()
     */
    @Override
    public K getId() {
        return id;
    }

    /**
     * Gets the modification date.
     * @return the modification date
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#getModificationDate()
     */
    @Override
    @Generated("Generated when updating the data into the database")
    public Date getModificationDate() {
        return modificationDate;
    }

    /**
     * Hash code.
     * @return the int
     */
    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * On insert.
     */
    @SuppressWarnings("unused")
    @PrePersist
    private void onInsert() {
        creationDate = new Date();
        modificationDate = creationDate;
    }

    /**
     * On update.
     */
    @SuppressWarnings("unused")
    @PreUpdate
    private void onUpdate() {
        modificationDate = new Date();
    }

    /**
     * Sets the creation date.
     * @param value the new creation date
     */
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

    /**
     * Sets the id.
     * @param value the new id
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#setId(java.io.Serializable)
     */
    @Override
    public void setId(final K value) {
        id = value;
    }

    /**
     * Sets the modification date.
     * @param value the new modification date
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PersistentObject#setModificationDate(java.util.Date)
     */
    @Override
    public void setModificationDate(final Date value) {
        modificationDate = value;
    }

    /**
     * To string.
     * @return the string
     */
    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
