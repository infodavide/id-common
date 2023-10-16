package org.infodavid.commons.model;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The Class User.</br>
 * Password of the user is always hashed using MD5 in the DTO and database.
 */
@Entity
@Table(name = "users")
public class User extends NamedObject<Long> implements PropertiesContainer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6579481270902648373L;

    /** The connections count. */
    @Column(name = "connections_count")
    private long connectionsCount;

    /** The display name. */
    @Column(name = "display_name", length = 96)
    private String displayName;

    /** The email. */
    @Column(name = "email", length = 255)
    private String email;

    /** The expiration date. */
    @Column(name = "edate")
    private LocalDate expirationDate;

    /** The groups. */
    @OneToMany(targetEntity = Group.class, cascade = CascadeType.ALL)
    @JoinTable(name = "users_groups", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<Group> groups;

    /** The last connection date. */
    @Column(name = "lcdate")
    private Date lastConnectionDate;

    /** The last IP address. */
    @Column(name = "last_ip", length = 48)
    private String lastIp;

    /** The locked. */
    @Column(name = "lcoked")
    private boolean locked;

    /** The password. */
    @Column(name = "password", length = 48)
    private String password;

    /** The properties. */
    @OneToMany(targetEntity = Property.class, cascade = CascadeType.ALL)
    @JoinTable(name = "users_properties", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private final Map<String, Property> properties;

    /** The role. */
    @Column(name = "role", length = 16)
    private String role;

    /**
     * The Constructor.
     */
    public User() {
        properties = new HashMap<>();
    }

    /**
     * Instantiates a new user.
     * @param id the identifier
     */
    public User(final Long id) {
        super(id);
        properties = new HashMap<>();
    }

    /**
     * Instantiates a new user.
     * @param source the source
     */
    public User(final User source) {
        super(source);
        connectionsCount = source.connectionsCount;
        displayName = source.displayName;
        email = source.email;
        expirationDate = source.expirationDate;
        lastConnectionDate = source.lastConnectionDate;
        lastIp = source.lastIp;
        locked = source.locked;
        password = source.password;
        properties = new HashMap<>(source.properties);
        role = source.role;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.NamedObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof final User other)) {
            return false;
        }

        if (getName() == null) { // NOSONAR
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }

        return true;
    }

    /**
     * Gets the connections count.
     * @return the connections count
     */
    @NotNull
    @Min(value = 0)
    @Generated("Set by the service")
    public long getConnectionsCount() {
        return connectionsCount;
    }

    /**
     * Gets the display name.
     * @return the display name
     */
    @NotNull
    @Size(min = 2, max = Constants.USER_DISPLAY_NAME_MAX_LENGTH)
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the email.
     * @return the email
     */
    @Size(min = 0, max = Constants.EMAIL_MAX_LENGTH)
    public String getEmail() {
        return email;
    }

    /**
     * Gets the expiration date.
     * @return the expiration date
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Gets the groups.
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.AbstractObject#getId()
     */
    @Override
    @Min(1)
    public Long getId() {
        return super.getId();
    }

    /**
     * Gets the last connection date.
     * @return the last connection date
     */
    @Generated("Set by the service")
    public Date getLastConnectionDate() {
        return lastConnectionDate;
    }

    /**
     * Gets the last IP address.
     * @return the last IP address
     */
    @Size(min = 0, max = Constants.LAST_IP_MAX_LENGTH)
    @Generated("Set by the service")
    public String getLastIp() {
        return lastIp;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.NamedObject#getName()
     */
    @Override
    @NotNull
    @Size(min = Constants.USER_NAME_MIN_LENGTH, max = Constants.USER_NAME_MAX_LENGTH)
    public String getName() {
        return super.getName();
    }

    /**
     * Gets the password.
     * @return the password
     */
    @NotNull
    @Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    public String getPassword() {
        return password;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.PropertiesContainer#getProperties()
     */
    @Override
    public Map<String, Property> getProperties() {
        return properties;
    }

    /**
     * Gets the role.
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.model.NamedObject#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = super.hashCode();
        return prime * result + (getName() == null ? 0 : getName().hashCode()); // NOSONAR Keep null check
    }

    /**
     * Checks if is expired.
     * @return true, if is expired
     */
    @Transient
    @Generated("Set by the service")
    public boolean isExpired() {
        final LocalDate expiration = getExpirationDate();

        return expiration != null && !expiration.isAfter(LocalDate.now());
    }

    /**
     * Checks if is locked.
     * @return true, if is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the connections count.
     * @param value the new connections count
     */
    public void setConnectionsCount(final long value) {
        connectionsCount = value;
    }

    /**
     * Sets the display name.
     * @param value the new display name
     */
    public void setDisplayName(final String value) {
        displayName = value;
    }

    /**
     * Sets the email.
     * @param value the new email
     */
    public void setEmail(final String value) {
        email = value;
    }

    /**
     * Sets the expiration date.
     * @param value the new expiration date
     */
    public void setExpirationDate(final LocalDate value) {
        expirationDate = value;
    }

    /**
     * Sets the groups.
     * @param groups the new groups
     */
    public void setGroups(final List<Group> groups) {
        this.groups = groups;
    }

    /**
     * Sets the last connection date.
     * @param value the new last connection date
     */
    public void setLastConnectionDate(final Date value) {
        lastConnectionDate = value;
    }

    /**
     * Sets the last IP address.
     * @param value the new last IP address
     */
    public void setLastIp(final String value) {
        lastIp = value;
    }

    /**
     * Sets the locked.
     * @param value the new locked
     */
    public void setLocked(final boolean value) {
        locked = value;
    }

    /**
     * Sets the password.
     * @param value the new password
     */
    public void setPassword(final String value) {
        password = value;
    }

    /**
     * Sets the role.
     * @param role the new role
     */
    public void setRole(final String role) {
        this.role = role;
    }
}
