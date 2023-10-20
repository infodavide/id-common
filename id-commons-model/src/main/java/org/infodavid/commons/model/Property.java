package org.infodavid.commons.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

/**
 * The Class Property.
 */
@MappedSuperclass
@AttributeOverrides(value = {
        @AttributeOverride(name = "name", column = @Column(name = "name", length = 48, nullable = false))
})
public class Property extends NamedObject<Long> implements Comparable<Property> { // NOSONAR equals and hashcode from super class

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 4650115076573283055L;

    /** The default value. */
    @Column(name = "default_value", length = 1024, nullable = true)
    private String defaultValue;

    /** The deletable flag. */
    @Column(name = "deletable", columnDefinition = "boolean default true")
    private boolean deletable = true;

    /** The editable flag. */
    @Column(name = "editable", columnDefinition = "boolean default true")
    private boolean editable = true;

    /** The label. */
    @Column(name = "label", length = 128, nullable = true)
    private String label;

    /** The maximum. */
    @Column(name = "maxi", nullable = true)
    private Double maximum;

    /** The minimum. */
    @Column(name = "mini", nullable = true)
    private Double minimum;

    /** The scope. */
    @Column(name = "scope", length = 48, nullable = true)
    private String scope;

    /** The type. */
    @Column(name = "data_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType type;

    /** The type definition. */
    @Column(name = "data_type_def", length = 255, nullable = true)
    private String typeDefinition;

    /** The value. */
    @Column(name = "data", length = 1024, nullable = true)
    private String value;

    /**
     * Instantiates a new property.
     */
    public Property() {
    }

    /**
     * The Constructor.
     * @param id the identifier
     */
    public Property(final Long id) {
        super(id);
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
        editable = source.editable;
        deletable = source.deletable;
        maximum = source.maximum;
        minimum = source.minimum;
        defaultValue = source.defaultValue;
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final boolean value) {
        this(name, PropertyType.BOOLEAN);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final byte value) {
        this(name, PropertyType.BYTE);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final double value) {
        this(name, PropertyType.DOUBLE);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final float value) {
        this(name, PropertyType.FLOAT);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final int value) {
        this(name, PropertyType.INTEGER);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final long value) {
        this(name, PropertyType.LONG);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final Number value) {
        this(name, PropertyType.LONG);

        if (value instanceof Byte) {
            setType(PropertyType.BYTE);
        } else if (value instanceof Short) {
            setType(PropertyType.SHORT);
        } else if (value instanceof Integer) {
            setType(PropertyType.INTEGER);
        } else if (value instanceof Long) {
            setType(PropertyType.LONG);
        } else if (value instanceof Float) {
            setType(PropertyType.FLOAT);
        } else if (value instanceof Double) {
            setType(PropertyType.DOUBLE);
        }

        setValue(value);
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

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param type  the type
     * @param value the value
     */
    public Property(final String name, final PropertyType type, final String value) {
        this(name, type);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final short value) {
        this(name, PropertyType.SHORT);
        setValue(value);
    }

    /**
     * Instantiates a new property.
     * @param name  the name
     * @param value the value
     */
    public Property(final String name, final String value) {
        this(name, PropertyType.STRING);
        setValue(value);
    }

    /**
     * Compare to.
     * @param o the other object to compare
     * @return the value
     */
    @Override
    public int compareTo(final Property o) { // NOSONAR
        return getName().compareTo(o.getName());
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
        return StringUtils.isNumeric(value) ? defaultValue : new Date(Long.parseLong(value));
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
     * Checks if is deletable.
     * @return true, if is deletable
     */
    public boolean isDeletable() {
        return deletable;
    }

    /**
     * Checks if is editable.
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Sets the default value.
     * @param defaultValue the new default value
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Sets the deletable.
     * @param deletable the new deletable
     */
    public void setDeletable(final boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * Sets the editable flag.
     * @param editable the editable flag to set
     */
    public void setEditable(final boolean editable) {
        this.editable = editable;
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
