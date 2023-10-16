package org.infodavid.commons.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.Column;

/**
 * The Class Property.
 */
public class Property extends NamedObject<Long> implements Comparable<Property> { // NOSONAR equals and hashcode from super class

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4650115076573283055L;

    /** The default value. */
    @Column(name = "default_value", length = 1024)
    private String defaultValue;

    /** The label. */
    @Column(name = "label", length = 128)
    private String label;

    /** The maximum. */
    @Column(name = "maxi")
    private Double maximum;

    /** The minimum. */
    @Column(name = "mini")
    private Double minimum;

    /** The read only. */
    @Column(name = "ro")
    private boolean readOnly;

    /** The scope. */
    @Column(name = "scope", length = 48)
    private String scope;

    /** The type. */
    @Column(name = "data_type", length = 48)
    private PropertyType type;

    /** The type definition. */
    @Column(name = "data_type_def", length = 255)
    private String typeDefinition;

    /** The value. */
    @Column(name = "data", length = 1024)
    private String value;

    /**
     * Instantiates a new property.
     */
    public Property() {
    }

    /**
     * Instantiates a new property.
     * @param source the source
     */
    public Property(final Property source) {
        super(source);
        type = source.type;
        typeDefinition = source.typeDefinition;
        scope = source.scope;
        value = source.value;
        label = source.label;
        maximum = source.maximum;
        minimum = source.minimum;
        defaultValue = source.defaultValue;
    }

    /**
     * Instantiates a new property with a null value.
     * @param name the name
     * @param type the type
     */
    public Property(final String name, final PropertyType type) {
        setName(name);
        setType(type);
    }

    /*
     * (non-javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    /**
     * Compare to.
     * @param o the o
     * @return the int
     */
    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final Property o) { // NOSONAR
        return getId().compareTo(o.getId());
    }

    /**
     * Gets the.
     * @param defaultValue the default value
     * @return the string
     */
    public String get(final String defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Gets the default value.
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the id.
     * @return the id
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.AbstractObject#getId()
     */
    @Override
    @Min(1)
    public Long getId() {
        return super.getId();
    }

    /**
     * Gets the label.
     * @return the label
     */
    @Size(min = 0, max = Constants.PROPERTY_LABEL_MAX_LENGTH)
    public String getLabel() {
        return label;
    }

    /**
     * Gets the maximum.
     * @return the maximum
     */
    public Double getMaximum() {
        return maximum;
    }

    /**
     * Gets the minimum.
     * @return the minimum
     */
    public Double getMinimum() {
        return minimum;
    }

    /**
     * Gets the name.
     * @return the name
     */
    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.NamedObject#getName()
     */
    @Override
    @NotNull
    @Size(min = 1, max = Constants.PROPERTY_NAME_MAX_LENGTH)
    public String getName() {
        return super.getName();
    }

    /**
     * Gets the scope.
     * @return the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * Gets the type.
     * @return the type
     */
    @NotNull
    public PropertyType getType() {
        return type;
    }

    /**
     * Gets the type definition.
     * @return the typeDefinition
     */
    public String getTypeDefinition() {
        return typeDefinition;
    }

    /**
     * Gets the value.
     * @return the value
     */
    @Size(min = 0, max = Constants.PROPERTY_VALUE_MAX_LENGTH)
    public String getValue() {
        return value;
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public boolean getValue(final boolean defaultValue) {
        return value == null ? defaultValue : Boolean.parseBoolean(value); // NOSONAR
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public byte getValue(final byte defaultValue) {
        return value == null ? defaultValue : Byte.parseByte(value);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public char getValue(final char defaultValue) {
        return value == null || value.length() == 0 ? defaultValue : value.charAt(0);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public Date getValue(final Date defaultValue) {
        return StringUtils.isNumeric(value) ? defaultValue : new Date(Long.valueOf(value));
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public double getValue(final double defaultValue) {
        return value == null ? defaultValue : Double.parseDouble(value);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public float getValue(final float defaultValue) {
        return value == null ? defaultValue : Float.parseFloat(value);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public int getValue(final int defaultValue) {
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public long getValue(final long defaultValue) {
        return value == null ? defaultValue : Long.parseLong(value);
    }

    /**
     * Gets the value.
     * @param defaultValue the default value
     * @return the value
     */
    public short getValue(final short defaultValue) {
        return value == null ? defaultValue : Short.parseShort(value);
    }

    /**
     * Checks if is read only.
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the default value.
     * @param defaultValue the new default value
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Sets the label.
     * @param label the new label
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Sets the maximum.
     * @param maximum the new maximum
     */
    public void setMaximum(final Double maximum) {
        this.maximum = maximum;
    }

    /**
     * Sets the minimum.
     * @param minimum the new minimum
     */
    public void setMinimum(final Double minimum) {
        this.minimum = minimum;
    }

    /**
     * Sets the read only.
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Sets the scope.
     * @param scope the scope to set
     */
    public void setScope(final String scope) {
        this.scope = scope;
    }

    /**
     * Sets the type.
     * @param type the type to set
     */
    public void setType(final PropertyType type) {
        this.type = type;
    }

    /**
     * Sets the type definition.
     * @param typeDefinition the typeDefinition to set
     */
    public void setTypeDefinition(final String typeDefinition) {
        this.typeDefinition = typeDefinition;
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final boolean value) {
        setValue(Boolean.toString(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final byte value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final char value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final Date value) {
        setValue(value == null ? null : String.valueOf(value.getTime()));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final double value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final float value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final int value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final long value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final Number value) {
        setValue(value == null ? null : String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final short value) {
        setValue(String.valueOf(value));
    }

    /**
     * Sets the value.
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }
}
