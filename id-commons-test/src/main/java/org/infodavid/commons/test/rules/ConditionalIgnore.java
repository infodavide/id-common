package org.infodavid.commons.test.rules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The Interface ConditionalIgnore.
 */
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ConditionalIgnoreRule.class)
public @interface ConditionalIgnore {

    /**
     * Condition.
     * @return the class<? extends ignore condition>
     */
    Class<? extends IgnoreCondition> condition();
}