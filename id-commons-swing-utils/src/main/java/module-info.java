module org.infodavid.commons.swing {
    exports org.infodavid.commons.swing.component;
    exports org.infodavid.commons.swing.action;
    exports org.infodavid.commons.swing.layout;
    exports org.infodavid.commons.swing;

    requires transitive ch.qos.logback.classic;
    requires transitive ch.qos.logback.core;
    requires transitive java.desktop;
    requires transitive java.prefs;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
}
