module org.infodavid.commons.test {
    exports org.infodavid.commons.test.mockito;
    exports org.infodavid.commons.test.rules;
    exports org.infodavid.commons.test;
    exports org.infodavid.commons.test.annotations;
    exports org.infodavid.commons.test.net;
    uses org.slf4j.spi.SLF4JServiceProvider;

    requires transitive mina.core;
    requires transitive org.apache.commons.io;
    requires transitive org.apache.commons.lang3;
    requires transitive org.mockito;
    requires transitive org.slf4j;
    requires transitive jul.to.slf4j;
    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;
    requires transitive java.logging;
    requires transitive org.apache.commons.logging;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.platform.engine;
    requires transitive org.junit.platform.launcher;
    requires transitive org.junit.platform.commons;
}
