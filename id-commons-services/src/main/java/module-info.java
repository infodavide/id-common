module org.infodavid.commons.service.api {
    exports org.infodavid.commons.service.listener;
    exports org.infodavid.commons.service.exception;
    exports org.infodavid.commons.service.security;
    exports org.infodavid.commons.service;

    requires transitive org.infodavid.commons.model;
    requires transitive java.validation;
    requires transitive mail;
    requires transitive org.slf4j;
}
