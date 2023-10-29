package org.infodavid.commons.system;

import static org.apache.commons.lang3.SystemUtils.IS_OS_FREE_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC;
import static org.apache.commons.lang3.SystemUtils.IS_OS_MAC_OSX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_NET_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_OPEN_BSD;
import static org.apache.commons.lang3.SystemUtils.IS_OS_UNIX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import static org.apache.commons.lang3.SystemUtils.OS_ARCH;
import static org.apache.commons.lang3.SystemUtils.OS_NAME;
import static org.apache.commons.lang3.SystemUtils.OS_VERSION;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * The Interface SystemUtilities.
 */
@SuppressWarnings("static-method")
public abstract class SystemUtilities {

    /** The Constant IS_NOT_SUPPORTED. */
    protected static final String IS_NOT_SUPPORTED = " is not supported";

    /** The Constant LINE_SEPARATOR. */
    protected static final String LINE_SEPARATOR = System.lineSeparator();

    /** The Constant METHOD_IS_NOT_SUPPORTED_ON. */
    protected static final String METHOD_IS_NOT_SUPPORTED_ON = "Method is not supported on ";

    /** The instance. */
    private static SystemUtilities singleton = null;

    /**
     * Gets the single instance.
     * @return single instance
     */
    public static synchronized SystemUtilities getInstance() {
        if (singleton == null) {
            if (IS_OS_FREE_BSD || IS_OS_LINUX || IS_OS_MAC || IS_OS_MAC_OSX || IS_OS_NET_BSD || IS_OS_OPEN_BSD || IS_OS_UNIX) {
                singleton = new LinuxSystemUtilities();
            } else if (IS_OS_WINDOWS) {
                singleton = new WindowsSystemUtilities();
            } else {
                throw new UnsupportedOperationException(OS_NAME + IS_NOT_SUPPORTED);
            }
        }

        return singleton;
    }

    /**
     * Instantiates a new system utilities.
     */
    protected SystemUtilities() {
        // noop
    }

    /**
     * Close quietly.
     * @param closeable the closeable
     */
    public void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (@SuppressWarnings("unused") final IOException | UnsupportedOperationException e) {
                // noop
            }
        }
    }

    /**
     * Execute.
     * @param command the command
     * @return the status code
     */
    protected int run(final String[] command) {
        int code = -1;

        try {
            code = CommandRunnerFactory.getInstance().run(command);
        } catch (final Exception e) {
            getLogger().warn("An error occured while running command: " + command, e); // NOSONAR Always written
        }

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("Exit code: {}", String.valueOf(code));
        }

        return code;
    }

    /**
     * Execute.
     * @param output  the output buffer
     * @param error   the error buffer
     * @param command the command
     * @return the status code
     */
    protected int run(final StringBuilder output, final StringBuilder error, final String... command) {
        int code = -1;

        try {
            code = CommandRunnerFactory.getInstance().run(output, error, command);
        } catch (final Exception e) {
            getLogger().warn("An error occured while running command: " + command, e); // NOSONAR Always written
        }

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("Exit code: {}", String.valueOf(code));
        }

        return code;
    }

    /**
     * Gets the architecture.
     * @return the architecture
     */
    public String getArchitecture() {
        return OS_ARCH;
    }

    /**
     * Gets the available time zones.
     * @return the available time zones
     */
    public String[] getAvailableTimeZones() {
        return TimeZone.getAvailableIDs();
    }

    /**
     * Gets the date time.
     * @return the date time
     */
    public ZonedDateTime getDateTime() {
        return ZonedDateTime.now();
    }

    /**
     * Gets the group principal.
     * @param name the name
     * @return the group principal or null
     * @throws UnsupportedOperationException the unsupported operation exception
     */
    @SuppressWarnings("resource")
    public GroupPrincipal getGroupPrincipal(final String name) throws UnsupportedOperationException {
        getLogger().debug("Trying to retrieve group principal by name: {}", name);
        FileSystem fs = null; // NOSONAR FS cannot always be closed

        try {
            fs = FileSystems.getDefault();

            return fs.getUserPrincipalLookupService().lookupPrincipalByGroupName(name);
        } catch (@SuppressWarnings("unused") final UserPrincipalNotFoundException e) {
            return null;
        } catch (final IOException e) {
            throw new UnsupportedOperationException(e);
        } finally {
            closeQuietly(fs);
        }
    }

    /**
     * Gets the logger.
     * @return the logger
     */
    protected abstract Logger getLogger();

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return OS_NAME;
    }

    /**
     * Gets the time zone.
     * @return the time zone
     */
    public String getTimeZone() {
        final String value = System.getProperty("user.timezone");

        if (StringUtils.isNotEmpty(value)) {
            return value;
        }

        return TimeZone.getDefault().getID();
    }

    /**
     * Gets the user principal.
     * @param name the name
     * @return the user principal or null
     * @throws UnsupportedOperationException the unsupported operation exception
     */
    @SuppressWarnings("resource")
    public UserPrincipal getUserPrincipal(final String name) throws UnsupportedOperationException {
        getLogger().debug("Trying to retrieve user principal by name: {}", name);
        FileSystem fs = null; // NOSONAR FS cannot always be closed

        try {
            fs = FileSystems.getDefault();

            return fs.getUserPrincipalLookupService().lookupPrincipalByName(name);
        } catch (@SuppressWarnings("unused") final UserPrincipalNotFoundException e) {
            return null;
        } catch (final IOException e) {
            throw new UnsupportedOperationException(e);
        } finally {
            closeQuietly(fs);
        }
    }

    /**
     * Gets the user principal.
     * @return the user principal or null
     * @throws UnsupportedOperationException the unsupported operation exception
     */
    public UserPrincipal getUserPrincipal() throws UnsupportedOperationException {
        return getUserPrincipal(getUserName());
    }

    /**
     * Gets the user name.
     * @return the user name
     */
    public String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * Gets the version.
     * @return the version
     */
    public String getVersion() {
        return OS_VERSION;
    }

    /**
     * Reboot.
     * @return the exit code
     */
    public abstract int reboot();

    /**
     * Sets the date time.
     * @param date the new date time
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public abstract void setDateTime(ZonedDateTime date) throws IOException;

    /**
     * Sets the time zone.
     * @param value the new time zone
     */
    public void setTimeZone(final String value) {
        final TimeZone tz = TimeZone.getTimeZone(value);

        if (tz == null) {

            throw new IllegalArgumentException("Time zone is not valid: " + value);
        }

        TimeZone.setDefault(tz);
    }

    /**
     * Shutdown.
     * @return the exit code
     */
    public abstract int shutdown();
}
