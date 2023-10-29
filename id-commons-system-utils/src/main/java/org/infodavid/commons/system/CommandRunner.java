package org.infodavid.commons.system;

import java.nio.file.Path;

/**
 * The Interface CommandRunner.
 */
public interface CommandRunner {

    /**
     * Execute.
     * @param command the command
     * @return the exit code
     */
    int run(String... command);

    /**
     * Execute.
     * @param output the output
     * @param error the error
     * @param workingDir the working directory
     * @param command the command line
     * @return the exit code
     */
    int run(StringBuilder output, StringBuilder error, Path workingDir, String[] command);

    /**
     * Execute.
     * @param output the output
     * @param error the error
     * @param command the command line
     * @return the exit code
     */
    int run(StringBuilder output, StringBuilder error, String[] command);
}
