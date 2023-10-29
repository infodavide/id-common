package org.infodavid.commons.system;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DefaultCommandExecutor.
 */
public final class DefaultCommandExecutor implements CommandRunner {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandExecutor.class);

    /**
     * Instantiates a new default command executor.
     */
    public DefaultCommandExecutor() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.CommandRunner#run(java.lang.String)
     */
    @Override
    public int run(final String... command) {
        return run(null, null, command);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.CommandRunner#run(java.lang.StringBuilder, java.lang.StringBuilder, java.nio.file.Path, java.lang.String[])
     */
    @Override
    public int run(final StringBuilder standardOutput, final StringBuilder errorOutput, final Path workingDirectory, final String[] command) {
        int code = -1;
        final CommandLine commandLine = new CommandLine(command[0]);

        if (command.length > 1) {
            for (int i = 1; i < command.length; i++) {
                commandLine.addArgument(command[i], false);
            }
        }

        final Executor executor = new DefaultExecutor();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing command: {}", commandLine);
        }

        if (workingDirectory != null) {
            executor.setWorkingDirectory(workingDirectory.toFile());
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ByteArrayOutputStream err = new ByteArrayOutputStream();
        String ouput = null;
        String error = null;

        try {
            executor.setStreamHandler(new PumpStreamHandler(out, err));
            code = executor.execute(commandLine);
        } catch (final Throwable e) { // NOSONAR
            LOGGER.warn("An error occured while executing command: {}, {}", StringUtils.join(command, ' '), e.getMessage()); // NOSONAR No format when using Throwable
        } finally {
            error = err.toString().trim();
            ouput = out.toString().trim();

            if (errorOutput != null) {
                errorOutput.append(error);
            }

            if (standardOutput != null) {
                standardOutput.append(ouput);
            }

            if (LOGGER.isDebugEnabled()) { // NOSONAR Null check to avoid warnings
                LOGGER.error(err.toString().trim());
                LOGGER.debug(out.toString().trim());
            }
        }

        if (code == 0) {
            LOGGER.debug("Command execution result:\n\tExit code: {}", String.valueOf(code)); // NOSONAR Always written
        } else if (StringUtils.isEmpty(error)) {
            LOGGER.warn("Exit code: {}", String.valueOf(code));// NOSONAR Always written

            return -1;
        } else if (StringUtils.isNotEmpty(ouput)) {
            LOGGER.warn("Exit code: {}, output: {}", String.valueOf(code), ouput); // NOSONAR Always written

            return -1;
        } else {
            LOGGER.warn("Exit code: {}, error: {}", String.valueOf(code), error); // NOSONAR Always written

            return -2;
        }

        return 0;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.CommandRunner#run(java.lang.StringBuilder, java.lang.StringBuilder, java.lang.String[])
     */
    @Override
    public int run(final StringBuilder output, final StringBuilder error, final String[] command) {
        return run(output, error, null, command);
    }
}
