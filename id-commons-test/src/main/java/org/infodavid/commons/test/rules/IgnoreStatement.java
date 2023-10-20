package org.infodavid.commons.test.rules;

import org.junit.Assume;
import org.junit.runners.model.Statement;

/**
 * The Class IgnoreStatement.
 */
class IgnoreStatement extends Statement {

    /** The condition. */
    private final IgnoreCondition condition;

    /**
     * Instantiates a new ignore statement.
     * @param condition the condition
     */
    IgnoreStatement(final IgnoreCondition condition) {
        this.condition = condition;
    }

    /*
     * (non-javadoc)
     * @see org.junit.runners.model.Statement#evaluate()
     */
    @Override
    public void evaluate() {
        if (condition == null) {
            return;
        }

        Assume.assumeTrue("Ignored by " + condition.getClass().getSimpleName(), false);
    }
}