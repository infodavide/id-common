module org.infodavid.commons.persistence.mock {
    exports org.infodavid.commons.persistence.impl.mock;

    requires transitive jakarta.persistence;
    requires transitive javafaker;
    requires transitive org.apache.commons.lang3;
    requires transitive org.infodavid.commons.model;
    requires transitive org.infodavid.commons.persistence.api;
}
