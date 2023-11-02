module org.infodavid.commons.test {
    exports org.infodavid.commons.test.mockito;
    exports org.infodavid.commons.test.rules;
    exports org.infodavid.commons.test;
    exports org.infodavid.commons.test.annotations;
    exports org.infodavid.commons.test.net;
    opens org.infodavid.commons.test.mockito;
    opens org.infodavid.commons.test.rules;
    opens org.infodavid.commons.test;
    opens org.infodavid.commons.test.annotations;
    opens org.infodavid.commons.test.net;

    requires transitive mina.core;
    requires transitive org.apache.commons.io;
    requires transitive org.apache.commons.lang3;
    requires transitive org.mockito;
    requires transitive org.slf4j;
    requires transitive org.slf4j.jul;
    requires transitive java.logging;
    requires transitive org.apache.commons.logging;
    requires transitive org.junit.jupiter.api;
    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.params;
    requires transitive org.junit.platform.engine;
    requires transitive org.junit.platform.launcher;
    requires transitive org.junit.platform.commons;
}
