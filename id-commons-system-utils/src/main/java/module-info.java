import org.infodavid.commons.system.CommandRunner;
import org.infodavid.commons.system.DefaultCommandExecutor;

module org.infodavid.commons.system {
    exports org.infodavid.commons.system;
    provides CommandRunner with DefaultCommandExecutor;
    uses CommandRunner;

    requires transitive com.sun.jna;
    requires transitive com.sun.jna.platform;
    requires transitive commons.exec;
    requires transitive java.prefs;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
}
