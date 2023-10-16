package org.infodavid.commons.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.infodavid.commons.model.EntityReference;

/**
 * The Class EntityReferenceValidator.
 */
@SuppressWarnings("rawtypes")
public class EntityReferenceValidator implements ConstraintValidator<ValidEntityReference,EntityReference> {

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(final ValidEntityReference constraintAnnotation) {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final EntityReference value, final ConstraintValidatorContext context) {
        return value == null || value.getId() != null; // NOSONAR
    }
}
