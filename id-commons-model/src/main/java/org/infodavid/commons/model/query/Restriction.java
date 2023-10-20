package org.infodavid.commons.model.query;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class Restriction.
 */
public class Restriction implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2419827317965217191L;

    /**
     * With.
     * @param field    the field
     * @param operator the operator
     * @param value    the value
     * @return the restriction
     */
    public static Restriction with(final String field, final RestrictionOperator operator, final String value) {
        return new Restriction(field, operator, value);
    }

    /**
     * With.
     * @param field    the field
     * @param operator the operator
     * @param value    the value
     * @return the restriction
     */
    public static Restriction with(final String field, final String operator, final String value) {
        return new Restriction(field, operator, value);
    }

    /** The and. */
    private Restriction and;

    /** The field. */
    private String field;

    /** The operator. */
    private RestrictionOperator operator;

    /** The or. */
    private Restriction or;

    /** The value. */
    private Serializable value;

    /**
     * Instantiates a new restriction.
     */
    public Restriction() {
        // noop
    }

    /**
     * Instantiates a new restriction.
     * @param field    the field
     * @param operator the operator
     * @param value    the value
     */
    public Restriction(final String field, final RestrictionOperator operator, final Serializable value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Instantiates a new restriction.
     * @param field    the field
     * @param operator the operator
     * @param value    the value
     */
    public Restriction(final String field, final String operator, final Serializable value) {
        this.field = field;
        this.operator = RestrictionOperator.valueOf(StringUtils.upperCase(operator));
        this.value = value;
    }

    /**
     * Gets the and.
     * @return the and
     */
    public Restriction getAnd() {
        return and;
    }

    /**
     * Gets the field.
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the operator.
     * @return the operator
     */
    public RestrictionOperator getOperator() {
        return operator;
    }

    /**
     * Gets the or.
     * @return the or
     */
    public Restriction getOr() {
        return or;
    }

    /**
     * Gets the value.
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the and.
     * @param and the new and
     */
    public void setAnd(final Restriction and) {
        this.and = and;
    }

    /**
     * Sets the field.
     * @param field the new field
     */
    public void setField(final String field) {
        this.field = field;
    }

    /**
     * Sets the operator.
     * @param operator the new operator
     */
    public void setOperator(final RestrictionOperator operator) {
        this.operator = operator;
    }

    /**
     * Sets the or.
     * @param or the new or
     */
    public void setOr(final Restriction or) {
        this.or = or;
    }

    /**
     * Sets the value.
     * @param value the new value
     */
    public void setValue(final Serializable value) {
        this.value = value;
    }
}
