package org.infodavid.commons.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.infodavid.commons.model.ObjectLink;

/**
 * The Class ObjectLinkValidator.
 */
public class ObjectLinkValidator implements ConstraintValidator<ValidObjectLink,ObjectLink> {

    /**
     * Instantiates a new validator.
     */
    public ObjectLinkValidator() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(final ValidObjectLink constraintAnnotation) {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final ObjectLink value, final ConstraintValidatorContext context) {
        return value == null || value.getId() != null; // NOSONAR
    }
}
