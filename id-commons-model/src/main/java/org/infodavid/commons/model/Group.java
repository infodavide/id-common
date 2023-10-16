package org.infodavid.commons.model;

import java.util.HashMap;
import java.util.Map;

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
public class Group extends NamedObject<Long> implements PropertiesContainer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6579481270902648373L;

    /** The display name. */
    @Column(name = "display_name", length = 96)
    private String displayName;

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
    public Group() {
        properties = new HashMap<>();
    }

    /**
     * Instantiates a new group.
     * @param id the identifier
     */
    public Group(final Long id) {
        super(id);
        properties = new HashMap<>();
    }

    /**
     * Instantiates a new group.
     * @param source the source
     */
    public Group(final Group source) {
        super(source);
        displayName = source.displayName;
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

        if (!(obj instanceof final Group other)) {
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
     * Sets the display name.
     * @param value the new display name
     */
    public void setDisplayName(final String value) {
        displayName = value;
    }

    /**
     * Sets the role.
     * @param role the new role
     */
    public void setRole(final String role) {
        this.role = role;
    }
}
