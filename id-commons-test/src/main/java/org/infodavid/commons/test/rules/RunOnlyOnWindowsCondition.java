package org.infodavid.commons.test.rules;

import org.apache.commons.lang3.SystemUtils;

/**
 * The Class RunOnlyOnWindowsCondition.
 */
public class RunOnlyOnWindowsCondition implements IgnoreCondition {

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.rules.IgnoreCondition#isSatisfied()
     */
    @Override
    public boolean isSatisfied() {
        return SystemUtils.IS_OS_WINDOWS;
    }
}
