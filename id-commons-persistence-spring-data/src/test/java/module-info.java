open module org.infodavid.commons.persistence.springdata {
    exports org.infodavid.commons.persistence.impl.springdata;
    exports org.infodavid.commons.persistence.impl.springdata.repository;

    requires transitive org.infodavid.commons.model;
    requires transitive org.infodavid.commons.persistence.api;
    requires transitive org.hsqldb;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.zaxxer.hikari;
    requires transitive jakarta.persistence;
    requires transitive jakarta.transaction;
    requires transitive jakarta.xml.bind;
    requires transitive java.sql;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
    requires transitive spring.beans;
    requires transitive spring.context;
    requires transitive spring.aop;
    requires transitive spring.expression;
    requires transitive spring.jdbc;
    requires transitive spring.orm;
    requires transitive spring.tx;
    requires transitive spring.core;
    requires transitive spring.data.commons;
    requires transitive spring.data.jpa;
    requires transitive org.hibernate.orm.core;
    requires transitive org.hibernate.commons.annotations;
    requires transitive com.fasterxml.classmate;
    requires transitive net.bytebuddy;
    requires transitive org.infodavid.commons.test;
    requires transitive spring.test;
}
