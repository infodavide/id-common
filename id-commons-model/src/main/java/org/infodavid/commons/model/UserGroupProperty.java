package org.infodavid.commons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * The Class UserGroupProperty.
 */
@Entity
@Table(name = "usergroups_properties", uniqueConstraints = @UniqueConstraint(name = "unq_usergroupsproperties", columnNames = { "name", "usergroup_id" }))
public class UserGroupProperty extends Property {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7257037276459583330L;

    /**
     * The Constructor.
     */
    public UserGroupProperty() {
    }

    /**
     * Instantiates a new application setting.
     * @param source the source
     */
    public UserGroupProperty(final UserGroupProperty source) {
        super(source);
    }

    /**
     * The Constructor.
     * @param id the identifier
     */
    public UserGroupProperty(final Long id) {
        super(id);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final boolean value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final byte value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final double value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final float value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final int value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final long value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final Number value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name the name
     * @param type the type
     */
    public UserGroupProperty(final String name, final PropertyType type) {
        super(name, type);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param type  the type
     * @param value the value
     */
    public UserGroupProperty(final String name, final PropertyType type, final String value) {
        super(name, type, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final short value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public UserGroupProperty(final String name, final String value) {
        super(name, value);
    }
}
