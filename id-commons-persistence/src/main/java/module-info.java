module org.infodavid.commons.persistence.api {
    exports org.infodavid.commons.persistence.dao;
    opens org.infodavid.commons.persistence.dao;

    requires transitive org.infodavid.commons.model;
    requires transitive jakarta.persistence;
}
