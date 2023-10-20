package org.infodavid.commons.test.rules;

import org.apache.commons.lang3.StringUtils;
import org.infodavid.commons.test.annotations.ARetry;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The Class RetryRule.
 */
public class RetryRule implements MethodRule {

    /** The Constant RETRY_PROPERTY. */
    public static final String RETRY_PROPERTY = "retryTests";

    /** The test. */
    private final Object test;

    /**
     * Instantiates a new rule.
     * @param test the test
     */
    public RetryRule(final Object test) {
        this.test = test;
    }

    /*
     * (non-javadoc)
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement, org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) { // NOSONAR For testing
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String testName;

                if (test == null) {
                    testName = method.getName();
                }
                else {
                    testName = test.getClass().getSimpleName() + '.' + method.getName();
                }

                System.out.println("Evaluating test: " + testName); // NOSONAR For testing

                try {
                    base.evaluate();
                }
                catch (final Throwable t) {
                    ARetry retry = method.getAnnotation(ARetry.class);
                    int retries = 0;

                    if (retry == null && test != null) {
                        retry = test.getClass().getAnnotation(ARetry.class);
                    }

                    if (!isEnabled() || retry == null) {
                        throw t;
                    }

                    int maxRetries = 1;
                    Throwable error = null;

                    if (maxRetries <= 0) {
                        maxRetries = 1;
                    }

                    while (retries < maxRetries) {
                        retries++;
                        error = null;
                        System.out.println("Retrying test (" + retries + "): " + testName); // NOSONAR For testing

                        try {
                            base.evaluate();
                        }
                        catch (final Throwable t2) {
                            error = t2;
                        }
                    }

                    if (error != null) {
                        error.printStackTrace();

                        throw error;
                    }
                }
            }

            private boolean isEnabled() {
                return StringUtils.isNotEmpty(System.getProperty(RETRY_PROPERTY));
            }
        };
    }
}
