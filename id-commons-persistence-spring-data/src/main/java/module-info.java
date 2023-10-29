module org.infodavid.commons.persistence.springdata {
    exports org.infodavid.commons.persistence.impl.springdata;
    exports org.infodavid.commons.persistence.impl.springdata.repository;
    opens org.infodavid.commons.persistence.impl.springdata to spring.core;
    opens org.infodavid.commons.persistence.impl.springdata.repository to spring.core;

    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.zaxxer.hikari;
    requires transitive jakarta.persistence;
    requires transitive java.sql;
    requires transitive org.apache.commons.lang3;
    requires transitive org.infodavid.commons.model;
    requires transitive org.infodavid.commons.persistence.api;
    requires transitive org.slf4j;
    requires transitive spring.beans;
    requires transitive spring.context;
    requires transitive spring.core;
    requires transitive spring.data.commons;
    requires transitive spring.data.jpa;
    requires transitive spring.jdbc;
    requires transitive spring.orm;
    requires transitive spring.tx;
}
