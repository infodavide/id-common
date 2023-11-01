import org.infodavid.commons.checksum.ChecksumGenerator;
import org.infodavid.commons.checksum.MD5ChecksumGenerator;
import org.infodavid.commons.checksum.SHA256ChecksumGenerator;
import org.infodavid.commons.checksum.SHA384ChecksumGenerator;
import org.infodavid.commons.checksum.SHA512ChecksumGenerator;

module org.infodavid.commons.checksum {
    exports org.infodavid.commons.checksum;
    opens org.infodavid.commons.checksum;
    provides ChecksumGenerator with MD5ChecksumGenerator, SHA256ChecksumGenerator, SHA384ChecksumGenerator, SHA512ChecksumGenerator;
    uses ChecksumGenerator;

    requires transitive commons.exec;
    requires transitive org.apache.commons.codec;
    requires transitive org.apache.commons.lang3;
    requires transitive org.slf4j;
}
