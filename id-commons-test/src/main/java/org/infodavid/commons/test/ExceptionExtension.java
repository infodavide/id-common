package org.infodavid.commons.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExceptionExtension.
 */
public class ExceptionExtension implements TestExecutionExceptionHandler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionExtension.class);

    @Override
    public void handleTestExecutionException(final ExtensionContext context, final Throwable throwable) throws Throwable {
        LOGGER.error(context.getDisplayName(), throwable);

        throw throwable;
    }
}
