package org.infodavid.commons.test.rules;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * The Class TimeoutRule.
 */
public class TimeoutRule extends Timeout {

    /** The annotated timeout. */
    private long annotatedTimeout = -1;

    /**
     * Instantiates a new timeout rule.
     * @param builder the builder
     */
    public TimeoutRule(final Builder builder) {
        super(builder);
    }

    /**
     * Instantiates a new timeout rule.
     * @param timeout  the timeout
     * @param timeUnit the time unit
     */
    public TimeoutRule(final long timeout, final TimeUnit timeUnit) {
        super(timeout, timeUnit);
    }

    /*
     * (non-javadoc)
     * @see org.junit.rules.Timeout#apply(org.junit.runners.model.Statement, org.junit.runner.Description)
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        final Test annotation = description.getAnnotation(Test.class);
        annotatedTimeout = -1;

        if (annotation != null && annotation.timeout() > 0) {
            annotatedTimeout = annotation.timeout();
        }

        return super.apply(base, description);
    }

    /*
     * (non-javadoc)
     * @see org.junit.rules.Timeout#createFailOnTimeoutStatement(org.junit.runners.model.Statement)
     */
    @Override
    protected Statement createFailOnTimeoutStatement(final Statement statement) throws Exception {
        long timeout;

        if (annotatedTimeout <= 0) {
            timeout = getTimeout(TimeUnit.MILLISECONDS);
        } else {
            timeout = annotatedTimeout;
        }

        if (timeout <= 0) {
            return statement;
        }

        return FailOnTimeout.builder().withTimeout(timeout, TimeUnit.MILLISECONDS).withLookingForStuckThread(getLookingForStuckThread()).build(statement);
    }
}
