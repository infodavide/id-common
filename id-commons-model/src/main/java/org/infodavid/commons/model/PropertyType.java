package org.infodavid.commons.model;

import java.math.BigInteger;
import java.util.Date;

/**
 * The Enum PropertyType.
 */
public enum PropertyType {

    /** The boolean. */
    BOOLEAN("Boolean", Boolean.class),
    /** The date. */
    DATE("Date and time", Date.class),
    /** The float. */
    FLOAT("Float", Float.class),
    /** The double. */
    DOUBLE("Double", Double.class),
    /** The enumeration type having its value in the type definition field. */
    ENUM("Enumeration", Enum.class),
    /** The byte. */
    BYTE("Byte", Byte.class),
    /** The short. */
    SHORT("Short", Short.class),
    /** The integer. */
    INTEGER("Integer", Integer.class),
    /** The long. */
    LONG("Long", Long.class),
    /** The unsigned byte. */
    UBYTE("Unsigned byte", Short.class),
    /** The unsigned short. */
    USHORT("Unsigned short", Integer.class),
    /** The unsigned integer. */
    UINTEGER("Unsigned integer", Long.class),
    /** The unsigned long. */
    ULONG("Unsigned long", BigInteger.class),
    /** The password. */
    PASSWORD("Password", String.class),
    /** The character. */
    CHARACTER("Character", Character.class),
    /** The string. */
    STRING("Text", String.class);

    /** The base class. */
    private final Class<?> baseClass;

    /** The label. */
    private final String label;

    /**
     * Instantiates a new property type.
     * @param label     the label
     * @param baseClass the base class
     */
    PropertyType(final String label, final Class<?> baseClass) {
        this.baseClass = baseClass;
        this.label = label;
    }

    /**
     * Gets the base class.
     * @return the base class
     */
    public Class<?> getBaseClass() {
        return baseClass;
    }

    /**
     * Gets the label.
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}
