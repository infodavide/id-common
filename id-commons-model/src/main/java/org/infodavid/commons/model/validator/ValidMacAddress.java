package org.infodavid.commons.model.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The Interface ValidMacAddress.
 */
@Documented
@Constraint(validatedBy = MacAdressValidator.class)
@Target({
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMacAddress {

    /**
     * Groups.
     * @return the class[]
     */
    Class<?>[] groups() default {};

    /**
     * Message.
     * @return the string
     */
    String message() default "MAC address is invalid";

    /**
     * Payload.
     * @return the class<? extends payload>[]
     */
    Class<? extends Payload>[] payload() default {};
}
