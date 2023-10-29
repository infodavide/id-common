package org.infodavid.commons.system;

import static org.apache.commons.lang3.ArrayUtils.add;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LinuxSystemUtilities.
 */
class LinuxSystemUtilities extends SystemUtilities {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LinuxSystemUtilities.class);

    /** The Constant BIN_SUDO. */
    private static final String BIN_SUDO = "sudo";

    /** The Constant BIN_TIMEDATECTL. */
    private static final String BIN_TIMEDATECTL = "timedatectl";

    /** The Constant REBOOT_COMMAND. Note: Full path of sudo command is not always the same. */
    private static final String[] REBOOT_COMMAND = {
        BIN_SUDO,
        "shutdown",
        "-r",
        "-t",
        "1"
    };

    /** The Constant SHUTDOWN_COMMAND. Note: Full path of sudo command is not always the same. */
    private static final String[] SHUTDOWN_COMMAND = {
        BIN_SUDO,
        "shutdown",
        "-h",
        "-t",
        "1"
    };

    /** The Constant DATE_SET_COMMAND. Note: Full path of sudo command is not always the same. */
    private static final String[] DATE_SET_COMMAND = {
        BIN_SUDO,
        "date",
        "-s"
    };

    /** The Constant TIMEDATECTL_SET_TZ_COMMAND. Note: Full path of sudo command is not always the same. */
    private static final String[] TIMEDATECTL_SET_TZ_COMMAND = {
        BIN_SUDO,
        BIN_TIMEDATECTL,
        "set-timezone"
    };

    /** The Constant TIMEDATECTL_SHOW_COMMAND. */
    private static final String[] TIMEDATECTL_SHOW_COMMAND = {
        BIN_TIMEDATECTL,
        "show"
    };

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#getTimeZone()
     */
    @Override
    public synchronized String getTimeZone() {
        final StringBuilder output = new StringBuilder();
        final StringBuilder error = new StringBuilder();

        if (run(output, error, TIMEDATECTL_SHOW_COMMAND) == 0) {
            for (final String line : output.toString().split("\n")) {
                if (StringUtils.startsWithIgnoreCase(line, "Timezone")) {
                    return StringUtils.substringAfter(line, "=");
                }
            }
        }

        return super.getTimeZone();
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#reboot()
     */
    @Override
    public synchronized int reboot() {
        return run(REBOOT_COMMAND);
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#setDateTime(java.time.ZonedDateTime)
     */
    @Override
    public synchronized void setDateTime(final ZonedDateTime date) throws IOException {
        if (date == null) {
            throw new IllegalArgumentException("No date specified");
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final String value = formatter.format(date);
        LOGGER.info("Setting date and time to: {}", value);

        if (run(add(DATE_SET_COMMAND, value)) != 0) {
            throw new IOException("Error in command when setting date and time");
        }
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#setTimeZone(java.lang.String)
     */
    @Override
    public synchronized void setTimeZone(final String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException("No value specified");
        }

        LOGGER.info("Setting time zone to: {}", value);

        if (run(add(TIMEDATECTL_SET_TZ_COMMAND, value)) != 0) {
            super.setTimeZone(value);
        }
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#shutdown()
     */
    @Override
    public synchronized int shutdown() {
        return run(SHUTDOWN_COMMAND);
    }
}
