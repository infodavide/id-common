package org.infodavid.commons.test.rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface ConditionalIgnore.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})
public @interface ConditionalIgnore {

    /**
     * Condition.
     * @return the class<? extends ignore condition>
     */
    Class<? extends IgnoreCondition> condition();
}