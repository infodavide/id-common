module org.infodavid.commons.test {
    exports org.infodavid.commons.test.mockito;
    exports org.infodavid.commons.test.rules;
    exports org.infodavid.commons.test;
    exports org.infodavid.commons.test.annotations;
    exports org.infodavid.commons.test.net;

    requires transitive mina.core;
    requires transitive org.apache.commons.io;
    requires transitive org.apache.commons.lang3;
    requires transitive org.mockito;
    requires transitive org.slf4j;
    requires transitive spring.core;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.platform.commons;
}
