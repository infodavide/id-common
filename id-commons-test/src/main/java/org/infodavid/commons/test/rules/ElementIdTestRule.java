package org.infodavid.commons.test.rules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The Class ElementIdTestRule.
 */
public class ElementIdTestRule implements MethodRule {

    /** The id. */
    private String id;

    /*
     * (non-Javadoc)
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement, org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    @Override
    public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                }
                catch (final Throwable e) {
                    System.err.println("Current element ID: " + id); // NOSONAR For testing

                    throw e;
                }
            }
        };
    }

    /**
     * Gets the id.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * @param value the new id
     */
    public void setId(final String value) {
        id = value;
    }
}
