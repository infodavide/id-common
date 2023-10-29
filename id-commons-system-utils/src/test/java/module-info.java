open module org.infodavid.commons.system {
    exports org.infodavid.commons.system;

    requires transitive com.sun.jna;
    requires transitive com.sun.jna.platform;
    requires transitive commons.exec;
    requires transitive java.prefs;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
    requires transitive org.infodavid.commons.test;
}
