package org.infodavid.commons.model.query;

/**
 * The Enum RestrictionOperator.
 */
public enum RestrictionOperator {

    /** The equals. */
    EQ("Equals"),
    /** The not equals. */
    NOT_EQ("Not equals"),
    /** The insensitive equals. */
    INSENSITIVE_EQ("Equals"),
    /** The not insensitive equals. */
    INSENSITIVE_NOT_EQ("Not equals"),
    /** The greater than. */
    GT("Greater than"),
    /** The greater than or equals. */
    GE("Greater than or equals"),
    /** The less than. */
    LT("Less than"),
    /** The less than or equals. */
    LE("Less than or equals"),
    /** The null. */
    NULL("Null"),
    /** The not null. */
    NOT_NULL("Not null"),
    /** The like. */
    LIKE("Like"),
    /** The insensitive like. */
    INSENSITIVE_LIKE("Like"),
    /** The not like. */
    NOT_LIKE("Not like"),
    /** The not insensitive like. */
    INSENSITIVE_NOT_LIKE("Not like");

    /** The label. */
    private final String label;

    /**
     * Instantiates a new restriction operator.
     * @param label the label
     */
    RestrictionOperator(final String label) {
        this.label = label;
    }

    /**
     * Gets the label.
     * @return the label
     */
    public String getLabel() {
        return label;
    }

}
