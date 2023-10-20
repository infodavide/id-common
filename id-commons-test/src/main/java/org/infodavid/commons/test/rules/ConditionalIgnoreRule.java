package org.infodavid.commons.test.rules;

import java.lang.reflect.Modifier;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * The Class ConditionalIgnoreRule.
 */
public class ConditionalIgnoreRule implements MethodRule {

    /**
     * Gets the ignore condition.
     * @param method   the method
     * @param instance the instance
     * @return the ignore condition
     */
    private static IgnoreCondition getIgnoreContition(final FrameworkMethod method, final Object instance) {
        return newCondition(method.getAnnotation(ConditionalIgnore.class), instance);
    }

    /**
     * Checks for conditional ignore annotation.
     * @param method the method
     * @return true, if successful
     */
    private static boolean hasConditionalIgnoreAnnotation(final FrameworkMethod method) {
        if (method == null) {
            return false;
        }

        return method.getAnnotation(ConditionalIgnore.class) != null;
    }

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
            e.printStackTrace(); //NOSONAR For testing

            throw e;
        } catch (final Exception e) {
            e.printStackTrace(); //NOSONAR For testing

            throw new RuntimeException(e); //NOSONAR For testing
        }
    }

    /*
     * (non-javadoc)
     * @see org.junit.rules.MethodRule#apply(org.junit.runners.model.Statement, org.junit.runners.model.FrameworkMethod, java.lang.Object)
     */
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        final Statement result = base;

        if (hasConditionalIgnoreAnnotation(method)) {
            final IgnoreCondition condition = getIgnoreContition(method, target);

            if (condition == null) {
                return result;
            }

            if (!condition.isSatisfied()) {
                return new IgnoreStatement(condition);
            }
        }

        return result;
    }
}
