import org.infodavid.commons.system.CommandRunner;

module org.infodavid.commons.jdk {
    exports org.infodavid.commons.jdk;
    opens org.infodavid.commons.jdk to java.lang, java.base;
    uses CommandRunner;

    requires java.base;
    requires java.management;
    requires transitive org.apache.commons.io;
    requires transitive org.apache.commons.lang3;
    requires transitive org.infodavid.commons.system;
    requires transitive org.infodavid.commons.utility;
    requires transitive org.slf4j;
}
