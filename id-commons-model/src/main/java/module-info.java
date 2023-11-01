module org.infodavid.commons.model {
    exports org.infodavid.commons.model.validator;
    exports org.infodavid.commons.model.query;
    exports org.infodavid.commons.model;
    exports org.infodavid.commons.model.annotation;
    opens org.infodavid.commons.model;
    opens org.infodavid.commons.model.query;
    opens org.infodavid.commons.model.validator;
    opens org.infodavid.commons.model.annotation;

    requires transitive jakarta.persistence;
    requires transitive java.compiler;
    requires transitive java.validation;
    requires transitive org.apache.commons.lang3;
}
