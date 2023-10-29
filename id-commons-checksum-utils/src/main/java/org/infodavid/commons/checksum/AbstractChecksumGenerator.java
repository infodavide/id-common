package org.infodavid.commons.checksum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractChecksumGenerator.
 */
public abstract class AbstractChecksumGenerator implements ChecksumGenerator {

    /** The Constant ALLOWED_CHARS. */
    private static final String ALLOWED_CHARS = "ABCDEFabcdef0123456789";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChecksumGenerator.class);

    /** True is command is supported. */
    private boolean commandSupported;

    /**
     * Instantiates a new abstract checksum generator.
     * @throws InterruptedException the interrupted exception
     */
    protected AbstractChecksumGenerator() throws InterruptedException {
        final String command = getCommand();

        if (StringUtils.isEmpty(command)) {
            return;
        }

        final String[] commandLine = {
                command, "--help"
        };

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Checking if command exists on the system ({})", command);
        }

        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
            processBuilder.redirectErrorStream(true);
            final Process process = processBuilder.start();
            final int code = process.waitFor();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Return code: {}", String.valueOf(code));
            }

            commandSupported = code == 0;
        } catch (final InterruptedException e) {
            throw e;
        } catch (final Exception e) {
            LOGGER.trace("An error occured while checking the presence of the command", e);
            commandSupported = false;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Command is supported: {}", String.valueOf(commandSupported)); // NOSONAR Always written
        }
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.ChecksumGenerator#getChecksum(java.nio.file.Path)
     */
    @Override
    public String getChecksum(final Path file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        if (!Files.exists(file)) { // NOSONAR Using NIO API
            throw new NoSuchFileException("File not found: " + file.toString());
        }

        if (isCommandSupported()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Using command : {} {}", getCommand(), file.toAbsolutePath());
            }

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            final CommandLine cmdLine = CommandLine.parse(getCommand() + StringUtils.SPACE + file.toAbsolutePath().toString());
            final DefaultExecutor executor = new DefaultExecutor();
            final PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            executor.setStreamHandler(streamHandler);
            final int code = executor.execute(cmdLine);

            if (code != 0 && errorStream.size() > 0) {
                throw new IOException(errorStream.toString(StandardCharsets.UTF_8));
            }

            final String output = outputStream.toString(StandardCharsets.UTF_8);
            final int index = StringUtils.indexOfAny(output, ALLOWED_CHARS);

            if (index == -1) {
                return StringUtils.substringBefore(output, " ");
            }

            return StringUtils.substringBefore(output.substring(index), " ");
        }

        try (InputStream in = Files.newInputStream(file)) {
            return getChecksum(in);
        }
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.checksum.ChecksumGenerator#isCommandSupported()
     */
    @Override
    public boolean isCommandSupported() {
        return commandSupported;
    }

    /**
     * Gets the checksum.
     * @param in the input
     * @return the checksum
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected abstract String getChecksum(InputStream in) throws IOException;

    /**
     * Gets the command.
     * @return the command
     */
    protected abstract String getCommand();
}
