package org.infodavid.commons.model;

import java.io.Serializable;
import java.util.Date;

/**
 * The Interface PersistentObject.
 * @param <K> the key type
 */
public interface PersistentObject<K extends Serializable> extends Serializable {
    /**
     * Gets the creation date.
     * @return the creation date
     */
    Date getCreationDate();

    /**
     * Gets the identifier.
     * @return the identifier
     */
    K getId();

    /**
     * Sets the creation date.
     * @param value the new creation date
     */
    void setCreationDate(Date value);

    /**
     * Sets the identifier.
     * @param id the identifier
     */
    void setId(K id);

    /**
     * Gets the modification date.
     * @return the modification date
     */
    Date getModificationDate();

    /**
     * Sets the modification date.
     * @param value the modification date
     */
    void setModificationDate(Date value);
}
