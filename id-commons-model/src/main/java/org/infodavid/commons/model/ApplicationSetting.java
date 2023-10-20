package org.infodavid.commons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * The Class ApplicationSetting.
 */
@Entity
@Table(name = "application_settings", uniqueConstraints = @UniqueConstraint(name = "unq_applicationsettings", columnNames = "name"))
public class ApplicationSetting extends Property {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7257037276459583330L;

    /**
     * The Constructor.
     */
    public ApplicationSetting() {
    }

    /**
     * Instantiates a new application setting.
     * @param source the source
     */
    public ApplicationSetting(final ApplicationSetting source) {
        super(source);
    }

    /**
     * The Constructor.
     * @param id the identifier
     */
    public ApplicationSetting(final Long id) {
        super(id);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final boolean value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final byte value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final double value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final float value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final int value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final long value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final Number value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name the name
     * @param type the type
     */
    public ApplicationSetting(final String name, final PropertyType type) {
        super(name, type);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param type  the type
     * @param value the value
     */
    public ApplicationSetting(final String name, final PropertyType type, final String value) {
        super(name, type, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final short value) {
        super(name, value);
    }

    /**
     * Instantiates a new application setting.
     * @param name  the name
     * @param value the value
     */
    public ApplicationSetting(final String name, final String value) {
        super(name, value);
    }
}
