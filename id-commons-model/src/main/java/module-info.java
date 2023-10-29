module org.infodavid.commons.model {
    exports org.infodavid.commons.model.validator;
    exports org.infodavid.commons.model.query;
    exports org.infodavid.commons.model;
    exports org.infodavid.commons.model.annotation;
    opens org.infodavid.commons.model to org.hibernate.orm.core, spring.core, org.apache.commons.lang3;
    opens org.infodavid.commons.model.query to org.hibernate.orm.core, spring.core, org.apache.commons.lang3;
    opens org.infodavid.commons.model.validator to org.hibernate.orm.core, spring.core, org.apache.commons.lang3;
    opens org.infodavid.commons.model.annotation to org.hibernate.orm.core, spring.core, org.apache.commons.lang3;

    requires transitive jakarta.persistence;
    requires transitive java.compiler;
    requires transitive java.validation;
    requires transitive org.apache.commons.lang3;
}
