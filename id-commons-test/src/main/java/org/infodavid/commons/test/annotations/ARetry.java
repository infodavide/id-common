package org.infodavid.commons.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface ARetry.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {
        ElementType.METHOD, ElementType.TYPE
})
@Inherited
public @interface ARetry {

    /**
     * Value.
     * @return the int
     */
    int value() default 1;
}
