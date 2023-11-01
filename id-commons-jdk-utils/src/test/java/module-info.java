import org.infodavid.commons.system.CommandRunner;

open module org.infodavid.commons.jdk {
    exports org.infodavid.commons.jdk; 
    uses CommandRunner;
    
    requires java.base;
    requires java.management;
    requires transitive commons.exec;
    requires transitive org.apache.commons.io;
    requires transitive org.apache.commons.lang3;
    requires transitive org.infodavid.commons.system;
    requires transitive org.infodavid.commons.utility;
    requires transitive org.slf4j;
    requires org.infodavid.commons.test;
}
