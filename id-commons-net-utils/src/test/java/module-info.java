open module org.infodavid.commons.net {
    exports org.infodavid.commons.net;

    requires transitive com.github.benmanes.caffeine;
    requires transitive java.rmi;
    requires transitive org.apache.commons.lang3;
    requires transitive org.apache.commons.net;
    requires transitive org.infodavid.commons.concurrency;
    requires transitive org.infodavid.commons.io;
    requires transitive org.infodavid.commons.system;
    requires transitive org.infodavid.commons.utility;
    requires transitive org.slf4j;
    requires transitive org.slf4j.jul;
    requires transitive org.infodavid.commons.test;
}
