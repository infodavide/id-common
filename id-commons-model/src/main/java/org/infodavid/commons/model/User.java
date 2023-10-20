package org.infodavid.commons.model;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * The Class User.</br>
 * Password of the user is always hashed using MD5 in the DTO and database.
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "unq_users", columnNames = {
        "name"
}))
@SqlResultSetMapping(name = "UserReferenceSqlResultSetMapping", classes = {
        @ConstructorResult(targetClass = EntityReference.class, columns = {
                @ColumnResult(name = "id"), @ColumnResult(name = "name")
        })
})
public class User extends NamedObject<Long> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6579481270902648373L;

    /** The last connection date. */
    @Column(name = "connection_date", nullable = true)
    private Date connectionDate;

    /** The connections count. */
    @Column(name = "connections_count", columnDefinition = "bigint default 0")
    private long connectionsCount = 0;

    /** The display name. */
    @Column(name = "display_name", length = 96, nullable = false)
    private String displayName;

    /** The email. */
    @Column(name = "email", length = 255, nullable = true)
    private String email;

    /** The expiration date. */
    @Column(name = "expiration_date", nullable = true)
    private LocalDate expirationDate;

    /** The groups. */
    @ManyToMany(targetEntity = UserGroup.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_usergroups", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false), foreignKey = @ForeignKey(name = "fk_usersgroups_user"), inverseJoinColumns = @JoinColumn(name = "usergroup_id", referencedColumnName = "id", nullable = false), inverseForeignKey = @ForeignKey(name = "fk_usersgroups_usergroup"))
    @MapKey(name = "name")
    private Map<String, UserGroup> groups;

    /** The last IP address. */
    @Column(name = "ip", length = 48, nullable = true)
    private String ip;

    /** The locked. */
    @Column(name = "locked", columnDefinition = "boolean default false")
    private boolean locked = false;

    /** The password. */
    @Column(name = "password", length = 48, nullable = false)
    private String password;

    /** The properties. */
    @OneToMany(targetEntity = UserProperty.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_usersproperties_user"))
    @MapKey(name = "name")
    private Map<String, UserProperty> properties;

    /** The role. */
    @Column(name = "role", length = 16, nullable = true)
    private String role;

    /**
     * The Constructor.
     */
    public User() {
    }

    /**
     * Instantiates a new user.
     * @param id the identifier
     */
    public User(final Long id) {
        super(id);
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
        connectionDate = source.connectionDate;
        ip = source.ip;
        locked = source.locked;
        password = source.password;
        role = source.role;

        if (source.properties != null) {
            properties = new HashMap<>();

            for (final Entry<String, UserProperty> entry : source.properties.entrySet()) {
                properties.put(entry.getKey(), new UserProperty(entry.getValue()));
            }
        }
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
     * Gets the last connection date.
     * @return the date
     */
    @Generated("Set by the service")
    public Date getConnectionDate() {
        return connectionDate;
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
    public Map<String, UserGroup> getGroups() {
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
     * Gets the last IP address.
     * @return the IP address
     */
    @Size(min = 0, max = Constants.LAST_IP_MAX_LENGTH)
    @Generated("Set by the service")
    public String getIp() {
        return ip;
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

    /**
     * Gets the properties.
     * @return the properties
     */
    public Map<String, UserProperty> getProperties() {
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
     * On insert.
     */
    @SuppressWarnings("unused")
    @PrePersist
    private void onInsert() {
        connectionDate = null;
        connectionsCount = 0;
        ip = null;
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
    public void setGroups(final Map<String, UserGroup> groups) {
        this.groups = groups;
    }

    /**
     * Sets the last IP address.
     * @param value the new IP address
     */
    public void setIp(final String value) {
        ip = value;
    }

    /**
     * Sets the last connection date.
     * @param value the new date
     */
    public void setLastConnectionDate(final Date value) {
        connectionDate = value;
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
     * Sets the properties.
     * @param properties the properties
     */
    public void setProperties(final Map<String, UserProperty> properties) {
        this.properties = properties;
    }

    /**
     * Sets the role.
     * @param role the new role
     */
    public void setRole(final String role) {
        this.role = role;
    }
}
