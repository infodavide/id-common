package org.infodavid.commons.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The Class ObjectLink.
 */
public class ObjectLink implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -449021796863253704L;

    /**
     * Returns the value associated to the given model object.
     * @param model the model object
     * @return the link
     */
    public static ObjectLink valueOf(final NamedModelObject<? extends Serializable> model) {
        return new ObjectLink(model.getId(), model.getName());
    }

    /** The identifier. */
    private Serializable id;

    /** The label. */
    private String label;

    /**
     * Instantiates a new link.
     */
    public ObjectLink() {
    }

    /**
     * Instantiates a new link.
     * @param source the source
     */
    public ObjectLink(final ObjectLink source) {
        id = source.id;
        label = source.label;
    }

    /**
     * Instantiates a new link.
     * @param id    the identifier
     * @param label the label
     */
    public ObjectLink(final Serializable id, final String label) {
        this.id = id;
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

        if (!(obj instanceof ObjectLink)) {
            return false;
        }

        return super.equals(obj);
    }

    /**
     * Gets the identifier.
     * @return the identifier
     */
    public Serializable getId() {
        return id;
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

    public void setId(final Serializable id) {
        this.id = id;
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
