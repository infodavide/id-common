package org.infodavid.commons.test.rules;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * The Class ConditionalIgnoreRule.
 */
public class ConditionalIgnoreRule implements ExecutionCondition {

    /**
     * New condition.
     * @param annotation the annotation
     * @param instance   the instance
     * @return the ignore condition
     */
    private static IgnoreCondition newCondition(final ConditionalIgnore annotation, final Object instance) {
        if (annotation == null) {
            return null;
        }

        final Class<? extends IgnoreCondition> cond = annotation.condition();

        if (cond == null) {
            return null;
        }

        try {
            if (cond.isMemberClass()) {
                if (Modifier.isStatic(cond.getModifiers())) {
                    return cond.getDeclaredConstructor().newInstance();
                }

                if (instance != null && instance.getClass().isAssignableFrom(cond.getDeclaringClass())) {
                    return cond.getDeclaredConstructor(instance.getClass()).newInstance(instance);
                }

                throw new IllegalArgumentException("Conditional class: " + cond.getName() + " was an inner member class however it was not declared inside the test case using it. Either make this class a static class (by adding static keyword), standalone class (by declaring it in it's own file) or move it inside the test case using it");
            }

            return cond.getConstructor().newInstance();
        } catch (final RuntimeException e) {
            e.printStackTrace(); // NOSONAR For testing

            throw e;
        } catch (final Exception e) {
            e.printStackTrace(); // NOSONAR For testing

            throw new RuntimeException(e); // NOSONAR For testing
        }
    }

    /**
     * Instantiates a new rule.
     */
    public ConditionalIgnoreRule() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.junit.jupiter.api.extension.ExecutionCondition#evaluateExecutionCondition(org.junit.jupiter.api.extension.ExtensionContext)
     */
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(final ExtensionContext context) {
        final Optional<Method> method = context.getTestMethod();

        if (method.isEmpty()) {
            return ConditionEvaluationResult.enabled("No method found, moving on...");
        }

        final ConditionalIgnore annotation = method.get().getAnnotation(ConditionalIgnore.class);
        final Optional<Object> instance = context.getTestInstance();

        if (annotation == null || instance.isEmpty()) {
            return ConditionEvaluationResult.enabled("No instance or no condition annotation found, moving on...");
        }

        final IgnoreCondition condition = newCondition(annotation, instance.get());

        if (condition == null) {
            return ConditionEvaluationResult.enabled("No condition on annotation, moving on...");
        }

        if (!condition.isSatisfied()) {
            return ConditionEvaluationResult.disabled("Condition not satisfied");
        }

        return ConditionEvaluationResult.disabled("Condition not satisfied, moving on...");
    }
}
