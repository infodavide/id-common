package org.infodavid.commons.test.rules;

import org.apache.commons.lang3.SystemUtils;

/**
 * The Class RunOnlyOnMacCondition.
 */
public class RunOnlyOnMacCondition implements IgnoreCondition {

    /*
     * (non-javadoc)
     * @see org.infodavid.commons.test.rules.IgnoreCondition#isSatisfied()
     */
    @Override
    public boolean isSatisfied() {
        return SystemUtils.IS_OS_MAC;
    }
}