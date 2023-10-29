package org.infodavid.commons.system;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.platform.win32.Advapi32Util; // NOSONAR JNA
import com.sun.jna.platform.win32.Advapi32Util.Account; // NOSONAR JNA
import com.sun.jna.platform.win32.Win32Exception; // NOSONAR JNA
import com.sun.jna.platform.win32.WinBase.SYSTEMTIME; // NOSONAR JNA

/**
 * The Class WindowsSystemUtilities.
 */
class WindowsSystemUtilities extends SystemUtilities {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WindowsSystemUtilities.class);

    /** The Constant REBOOT_COMMAND. */
    private static final String[] REBOOT_COMMAND = {
        "shutdown",
        "/r"
    };

    /** The Constant SHUTDOWN_COMMAND. */
    private static final String[] SHUTDOWN_COMMAND = {
        "shutdown",
        "/s",
        "/t",
        "2"
    };

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.system.SystemUtilities#getGroupPrincipal(java.lang.String)
     */
    @SuppressWarnings("resource")
    @Override
    public GroupPrincipal getGroupPrincipal(final String name) throws UnsupportedOperationException {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("No name specified");
        }

        FileSystem fs = null; // NOSONAR FS cannot always be closed

        if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            try {
                LOGGER.debug("Trying to retrieve group principal by SID: {}", name);
                Account account;

                try {
                    account = Advapi32Util.getAccountByName(name);
                } catch (final Win32Exception e) {
                    LOGGER.debug("First method failed: {}", e.getMessage());
                    account = null;
                }

                if (account != null) {
                    LOGGER.debug("Using lookup with default filesystem...");
                    fs = FileSystems.getDefault();

                    return fs.getUserPrincipalLookupService().lookupPrincipalByGroupName(account.name);
                }
            } catch (final IOException e) {
                throw new UnsupportedOperationException(e);
            } finally {
                closeQuietly(fs);
            }
        }

        return super.getGroupPrincipal(name);
    }

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
     * @see org.infodavid.commons.system.SystemUtilities#getUserPrincipal(java.lang.String)
     */
    @SuppressWarnings("resource")
    @Override
    public UserPrincipal getUserPrincipal(final String name) throws UnsupportedOperationException {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("No name specified");
        }

        FileSystem fs = null; // NOSONAR FS cannot always be closed

        if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            try {
                LOGGER.debug("Trying to retrieve user principal by name: {}", name);
                Account account;

                try {
                    account = Advapi32Util.getAccountByName(name);
                } catch (final Win32Exception e) {
                    LOGGER.debug("First method failed: {}", e.getMessage());
                    account = null;
                }

                if (account != null) {
                    LOGGER.debug("Using lookup with default filesystem...");
                    fs = FileSystems.getDefault();

                    return fs.getUserPrincipalLookupService().lookupPrincipalByName(account.name);
                }
            } catch (final IOException e) {
                throw new UnsupportedOperationException(e);
            } finally {
                closeQuietly(fs);
            }
        }

        return super.getUserPrincipal(name);
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

        if (!setWindowsTime(date)) {
            throw new IOException("User is not allowed to change date or time or time synchronization is active (" +  com.sun.jna.platform.win32.Kernel32.INSTANCE.GetLastError() + ')');
        }
    }

    /**
     * Sets the date time.
     * @param date the date and time
     * @return true, if successful
     */
    @SuppressWarnings("static-method")
    private boolean setWindowsTime(final ZonedDateTime date) {
        final ZonedDateTime utc = date.withZoneSameInstant(ZoneId.of("Z"));
        final SYSTEMTIME systemTime = new SYSTEMTIME();
        systemTime.wYear = (short) utc.getYear();
        systemTime.wMonth = (short) utc.getMonthValue();
        systemTime.wDay = (short) utc.getDayOfMonth();
        systemTime.wHour = (short) utc.getHour();
        systemTime.wMinute = (short) utc.getMinute();
        systemTime.wSecond = (short) utc.getSecond();

        return com.sun.jna.platform.win32.Kernel32.INSTANCE.SetSystemTime(systemTime);
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
