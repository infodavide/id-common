package org.infodavid.commons.net;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class InterfaceDescription.
 */
public class InterfaceDescription implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8579351872062711339L;

    /**
     * Parses the string content.
     * @param content the content
     * @return the network interface info
     */
    public static InterfaceDescription parse(final String content) {
        // Example of input:
        // eth0;08:00:27:15:1A:20;False;192.168.1.65;255.255.255.0;192.168.1.254 or eth0;08:00:27:15:1A:20;True
        final InterfaceDescription result = new InterfaceDescription();
        final String[] parts = content.split(";");

        if (parts.length > 0) {
            byte part = 0;

            result.setName(parts[part++].trim());

            if (parts.length <= part) {
                return result;
            }

            result.setMacAddress(parts[part++].trim());

            if (parts.length <= part) {
                return result;
            }

            result.setDhcp(toBoolean(parts[part++].trim()));

            if (parts.length <= part) {
                return result;
            }

            result.setIpv4Address(parts[part++].trim());

            if (parts.length <= part) {
                return result;
            }

            result.setNetmask(parts[part++].trim());

            if (parts.length <= part) {
                return result;
            }

            result.setGateway(parts[part].trim());
        }

        return result;
    }

    /**
     * To boolean.
     * @param value the value
     * @return true, if successful
     */
    private static boolean toBoolean(final String value) {
        return "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value);
    }

    /** The connected. */
    private boolean connected;

    /** The DHCP enabled. */
    private boolean dhcp;

    /** The enabled. */
    private boolean enabled;

    /** The gateway. */
    private String gateway;

    /** The IP address. */
    private String ipv4Address;

    /** The MAC address. */
    private String macAddress;

    /** The name. */
    private String name;

    /** The netmask. */
    private String netmask;

    /**
     * Instantiates a new network interface info.
     */
    public InterfaceDescription() {
    }

    /**
     * Instantiates a new network interface info.
     * @param source the source
     */
    public InterfaceDescription(final InterfaceDescription source) {
        connected = source.connected;
        dhcp = source.dhcp;
        gateway = source.gateway;
        ipv4Address = source.ipv4Address;
        macAddress = source.macAddress;
        name = source.name;
        netmask = source.netmask;
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

        final InterfaceDescription other = (InterfaceDescription) obj;

        return Objects.equals(name, other.name) && Objects.equals(macAddress, other.macAddress);
    }

    /**
     * Gets the IPv4 of the gateway.
     * @return the gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * Gets the IP v4 address.
     * @return the address
     */
    public String getIpv4Address() {
        return ipv4Address;
    }

    /**
     * Gets the MAC address.
     * @return the address
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Gets the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the netmask.
     * @return the netmask
     */
    public String getNetmask() {
        return netmask;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(dhcp, gateway, ipv4Address, name, netmask);
    }

    /**
     * Checks if is connected.
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Checks if is DHCP enabled.
     * @return the flag
     */
    public boolean isDhcp() {
        return dhcp;
    }

    /**
     * Checks if is enabled.
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the connected.
     * @param connected the connected to set
     */
    public void setConnected(final boolean connected) {
        this.connected = connected;
    }

    /**
     * Sets the DHCP enabled.
     * @param dhcp the flag to set
     */
    public void setDhcp(final boolean dhcp) {
        this.dhcp = dhcp;
    }

    /**
     * Sets the enabled.
     * @param enabled the enabled to set
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets the IP v4 of the gateway.
     * @param gateway the IP v4 to set
     */
    public void setGateway(final String gateway) {
        this.gateway = gateway;
    }

    /**
     * Sets the IP v4 address.
     * @param ipv4Address the address to set
     */
    public void setIpv4Address(final String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    /**
     * Sets the MAC address.
     * @param macAddress the address to set
     */
    public void setMacAddress(final String macAddress) {
        this.macAddress = macAddress;
    }

    /**
     * Sets the name.
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the network mask.
     * @param netmask the mask to set
     */
    public void setNetmask(final String netmask) {
        this.netmask = netmask;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // Example of output:
        // eth0;08:00:27:15:1A:20;False;192.168.1.65;255.255.255.0;192.168.1.254 or eth0;08:00:27:15:1A:20;True
        final StringBuilder buffer = new StringBuilder();
        buffer.append(StringUtils.defaultString(name));
        buffer.append(';');
        buffer.append(StringUtils.defaultString(macAddress));
        buffer.append(';');
        buffer.append(dhcp ? "True" : "False");
        buffer.append(';');
        buffer.append(StringUtils.defaultString(ipv4Address));
        buffer.append(';');
        buffer.append(StringUtils.defaultString(netmask));
        buffer.append(';');
        buffer.append(StringUtils.defaultString(gateway));
        buffer.append(';');
        buffer.append(connected ? "True" : "False");

        return buffer.toString();
    }
}
