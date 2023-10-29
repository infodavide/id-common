package org.infodavid.commons.test.rules;

import org.apache.commons.lang3.SystemUtils;

/**
 * The Class NotRunOnWindowsCondition.
 */
public class NotRunOnWindowsCondition implements IgnoreCondition {

    /**
     * Instantiates a new condition.
     */
    public NotRunOnWindowsCondition() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.rules.IgnoreCondition#isSatisfied()
     */
    @Override
    public boolean isSatisfied() {
        return !SystemUtils.IS_OS_WINDOWS;
    }
}
