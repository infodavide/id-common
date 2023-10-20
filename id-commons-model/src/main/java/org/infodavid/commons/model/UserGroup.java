package org.infodavid.commons.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * The Class UserGroup.<
 */
@Entity
@Table(name = "user_groups", uniqueConstraints = @UniqueConstraint(name = "unq_usergroups", columnNames = "name"))
@SqlResultSetMapping(name = "UserGroupReferenceSqlResultSetMapping", classes = {
        @ConstructorResult(targetClass = EntityReference.class, columns = {
                @ColumnResult(name = "id"), @ColumnResult(name = "name")
        })
})
public class UserGroup extends NamedObject<Long> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6579481270902648373L;

    /** The display name. */
    @Column(name = "display_name", length = 96, nullable = false)
    private String displayName;

    /** The properties. */
    @OneToMany(targetEntity = UserGroupProperty.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usergroup_id", foreignKey = @ForeignKey(name = "fk_usergroupsproperties_usergroup"))
    @MapKey(name = "name")
    private Map<String, UserGroupProperty> properties;

    /** The role. */
    @Column(name = "role", length = 16, nullable = true)
    private String role;

    /**
     * The Constructor.
     */
    public UserGroup() {
    }

    /**
     * Instantiates a new group.
     * @param id the identifier
     */
    public UserGroup(final Long id) {
        super(id);
    }

    /**
     * Instantiates a new group.
     * @param source the source
     */
    public UserGroup(final UserGroup source) {
        super(source);
        displayName = source.displayName;
        role = source.role;

        if (source.properties != null) {
            properties = new HashMap<>();

            for (final Entry<String, UserGroupProperty> entry : source.properties.entrySet()) {
                properties.put(entry.getKey(), new UserGroupProperty(entry.getValue()));
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

        if (!(obj instanceof final UserGroup other)) {
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
     * Gets the display name.
     * @return the display name
     */
    @NotNull
    @Size(min = 2, max = Constants.USER_DISPLAY_NAME_MAX_LENGTH)
    public String getDisplayName() {
        return displayName;
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
     * Gets the properties.
     * @return the properties
     */
    public Map<String, UserGroupProperty> getProperties() {
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
     * Sets the display name.
     * @param value the new display name
     */
    public void setDisplayName(final String value) {
        displayName = value;
    }

    /**
     * Sets the properties.
     * @param properties the properties
     */
    public void setProperties(final Map<String, UserGroupProperty> properties) {
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
