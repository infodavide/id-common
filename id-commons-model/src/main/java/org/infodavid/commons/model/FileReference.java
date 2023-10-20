package org.infodavid.commons.model;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

/**
 * The Class FileReference.<br>
 * The identifier is the path relative to the logs directory.
 */
public class FileReference extends AbstractObject<String> implements Comparable<FileReference> { // NOSONAR Equals from super class

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 278322469116400765L;

    /** The name. */
    @Column(name = "name", length = 255)
    private String name;

    /** The path. */
    @Id
    @Column(name = "path", length = 1024)
    private String path;

    /** The size. */
    @Column(name = "size", length = 64)
    private String size;

    /**
     * Instantiates a new file.
     */
    public FileReference() {
    }

    /**
     * Instantiates a new file.
     * @param id the identifier
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public FileReference(final String id) throws IOException {
        super(id);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final FileReference o) { // NOSONAR
        return getId().compareTo(o.getId());
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

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof FileReference)) {
            return false;
        }

        final FileReference other = (FileReference)obj;

        if (!Objects.equals(name, other.name)) {
            return false;
        }

        return Objects.equals(path, other.path);
    }

    /*
     * (non-Javadoc)
     * @see org.infodavid.commons.model.AbstractObject#getId()
     */
    @Override
    @Size(min = 1)
    public String getId() {
        return super.getId();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the size.
     * @return the size
     */
    @Generated("Set by filesystem utilities")
    public String getSize() {
        return size;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (name == null ? 0 : name.hashCode());
        return prime * result + (path == null ? 0 : path.hashCode());
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param path the path to set
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * Sets the size.
     * @param size the size to set
     */
    public void setSize(final String size) {
        this.size = size;
    }
}
