package org.infodavid.commons.net;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The Class HostDescription.
 */
public class HostDescription implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5962641482545296732L;

    /**
     * Parse the data and try to instantiate a new HostDescription object.
     * @param content the content
     * @return the host info
     */
    public static HostDescription parse(final String content) { // NOSONAR No complexity, basic parsing
        // Example of input:
        // Application,SmartBox
        // Hostname,raspberry1
        // Info,4.19.0-13-686,0000001 SMP Debian 4.19.160-2 (2020-11-28),1
        // Locale,fr_FR,UTF-8
        // Time,13:33:49
        // Uptime,up 2 minutes
        // Addresses,eth0;08:00:27:15:1A:20;False;192.168.1.65;255.255.255.0;192.168.1.254 or Addresses,eth0;08:00:27:15:1A:20;True
        // SSID,
        HostDescription result = null;
        String[] rows = content.split("\\r?\\n");

        while (rows.length > 0 && !rows[0].startsWith("Application,")) {
            rows = ArrayUtils.remove(rows, 0);
        }

        if (rows.length > 0) {
            result = new HostDescription();
            byte row = 0;
            // row 1: Application
            String[] cells = rows[row++].split(",", 2);

            if (cells.length > 0) {
                result.setApplication(cells[1].trim());
            }

            if (rows.length <= row) {
                return result;
            }

            // row 2: Hostname
            cells = rows[row++].split(",", 2);

            if (cells.length > 1) {
                result.setName(cells[1].trim());
            }

            if (rows.length <= row) {
                return result;
            }

            // row 3: System information
            cells = rows[row++].split(",", 2);

            if (cells.length > 1) {
                result.setSystemInformation(cells[1].trim());
            }

            if (rows.length <= row) {
                return result;
            }

            // row 4: Locale information
            cells = rows[row++].split(",", 2);

            if (cells.length > 1) {
                result.setLocaleInformation(cells[1].trim());
            }

            if (rows.length <= row) {
                return result;
            }

            // row 5: Time
            cells = rows[row++].split(",", 2);

            if (cells.length > 1) {
                result.setTime(LocalTime.parse(cells[1].trim()));
            }

            if (rows.length <= row) {
                return result;
            }

            // row 6: Uptime
            cells = rows[row++].split(",", 2);

            if (cells.length > 1) {
                result.setUptime(cells[1].trim());
            }

            if (rows.length <= row) {
                return result;
            }

            // row 7: Interfaces
            cells = rows[row++].split(",");

            if (cells.length > 1) {
                // Example: Addresses,eth0;08:00:27:15:1A:20;False;192.168.1.65;255.255.255.0;192.168.1.254 or Addresses,eth0;08:00:27:15:1A:20;True
                InterfaceDescription[] interfacesInfo = new InterfaceDescription[0];

                for (byte i = 1; i < cells.length; i++) { // NOSONAR Continue are useful
                    final InterfaceDescription interfaceInfo = InterfaceDescription.parse(cells[i]);

                    if (interfaceInfo != null) {
                        interfacesInfo = ArrayUtils.add(interfacesInfo, interfaceInfo);
                    }
                }

                result.setInterfaces(interfacesInfo);
            }

            if (rows.length <= row) {
                return result;
            }

            // row 8: Wifi SSID
            cells = rows[row].split(",");

            if (cells.length > 1) {
                result.setSsid(cells[1].trim());
            }
        }

        return result;
    }

    /** The application. */
    private String application;

    /** The domain name servers. */
    private String[] domainNameServers;

    /** The interfaces. */
    private InterfaceDescription[] interfaces;

    /** The locale information. */
    private String localeInformation;

    /** The name. */
    private String name;

    /** The ssid. */
    private String ssid;

    /** The system information. */
    private String systemInformation;

    /** The time. */
    private LocalTime time;

    /** The uptime. */
    private String uptime;

    /**
     * Instantiates a new host info.
     */
    public HostDescription() {
    }

    /**
     * Instantiates a new host info.
     * @param source the source
     */
    public HostDescription(final HostDescription source) {
        application = source.application;
        localeInformation = source.localeInformation;
        ssid = source.ssid;
        systemInformation = source.systemInformation;
        time = source.time == null ? null : LocalTime.from(source.time);
        domainNameServers = source.domainNameServers == null ? null : source.domainNameServers.clone();
        name = source.name;
        uptime = source.uptime;

        if (source.interfaces == null) {
            interfaces = null;
        } else {
            interfaces = new InterfaceDescription[source.interfaces.length];

            for (byte i = 0; i < interfaces.length; i++) {
                interfaces[i] = new InterfaceDescription(source.interfaces[i]);
            }
        }
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final HostDescription other = (HostDescription) obj;

        if (!Arrays.equals(interfaces, other.interfaces)) {
            return false;
        }

        return Objects.equals(name, other.name);
    }

    /**
     * Gets the application.
     * @return the application
     */
    public String getApplication() {
        return application;
    }

    /**
     * Gets the domain name servers.
     * @return the domainNameServers
     */
    public String[] getDomainNameServers() {
        return domainNameServers;
    }

    /**
     * Gets the interfaces.
     * @return the interfaces
     */
    public InterfaceDescription[] getInterfaces() {
        return interfaces;
    }

    /**
     * Gets the locale information.
     * @return the localeInformation
     */
    public String getLocaleInformation() {
        return localeInformation;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the ssid.
     * @return the ssid
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * Gets the system information.
     * @return the systemInformation
     */
    public String getSystemInformation() {
        return systemInformation;
    }

    /**
     * Gets the time.
     * @return the time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Gets the uptime.
     * @return the uptime
     */
    public String getUptime() {
        return uptime;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(interfaces), name);
    }

    /**
     * Sets the application.
     * @param application the application to set
     */
    public void setApplication(final String application) {
        this.application = application;
    }

    /**
     * Sets the domain name servers.
     * @param domainNameServers the domainNameServers to set
     */
    public void setDomainNameServers(final String[] domainNameServers) {
        this.domainNameServers = domainNameServers;
    }

    /**
     * Sets the interfaces.
     * @param interfaces the interfaces to set
     */
    public void setInterfaces(final InterfaceDescription[] interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * Sets the locale information.
     * @param localeInformation the localeInformation to set
     */
    public void setLocaleInformation(final String localeInformation) {
        this.localeInformation = localeInformation;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the ssid.
     * @param ssid the ssid to set
     */
    public void setSsid(final String ssid) {
        this.ssid = ssid;
    }

    /**
     * Sets the system information.
     * @param systemInformation the systemInformation to set
     */
    public void setSystemInformation(final String systemInformation) {
        this.systemInformation = systemInformation;
    }

    /**
     * Sets the time.
     * @param time the time to set
     */
    public void setTime(final LocalTime time) {
        this.time = time;
    }

    /**
     * Sets the uptime.
     * @param uptime the time to set
     */
    public void setUptime(final String uptime) {
        this.uptime = uptime;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() { // NOSONAR No complexity
        // Example of output:
        // Application,SmartBox
        // Hostname,raspberry1
        // Info,4.19.0-13-686,0000001 SMP Debian 4.19.160-2 (2020-11-28),1
        // Locale,fr_FR,UTF-8
        // Time,13:33:49
        // Uptime,up 2 minutes
        // Addresses,eth0;08:00:27:15:1A:20;False;192.168.1.65;255.255.255.0;192.168.1.254 or Addresses,eth0;08:00:27:15:1A:20;True
        // Gateway,192.168.1.1
        // SSID,
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Application,");
        buffer.append(StringUtils.defaultString(application));
        buffer.append('\n');
        buffer.append("Hostname,");
        buffer.append(StringUtils.defaultString(name));
        buffer.append('\n');
        buffer.append("Info,");
        buffer.append(StringUtils.defaultString(systemInformation));
        buffer.append('\n');
        buffer.append("Locale,");
        buffer.append(StringUtils.defaultString(localeInformation));
        buffer.append('\n');
        buffer.append("Time,");
        buffer.append(time == null ? StringUtils.EMPTY : time.toString());
        buffer.append('\n');
        buffer.append("Uptime,");
        buffer.append(StringUtils.defaultString(uptime));
        buffer.append('\n');
        buffer.append("Addresses");

        if (interfaces == null) {
            buffer.append(',');
        } else {
            for (final InterfaceDescription info : interfaces) {
                buffer.append(',');
                buffer.append(info.toString());
            }
        }

        buffer.append('\n');
        buffer.append("SSID,");
        buffer.append(StringUtils.defaultString(ssid));
        buffer.append('\n');

        return buffer.toString();
    }
}
