package org.infodavid.commons.test.rules;

import org.apache.commons.lang3.SystemUtils;

/**
 * The Class RunOnlyOnLinuxCondition.
 */
public class RunOnlyOnLinuxCondition implements IgnoreCondition {

    /**
     * Instantiates a new condition.
     */
    public RunOnlyOnLinuxCondition() {
        // noop
    }

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.rules.IgnoreCondition#isSatisfied()
     */
    @Override
    public boolean isSatisfied() {
        return SystemUtils.IS_OS_LINUX;
    }
}
