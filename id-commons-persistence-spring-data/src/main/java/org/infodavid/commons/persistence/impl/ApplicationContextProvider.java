package org.infodavid.commons.persistence.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * The Class ApplicationContextProvider.
 */
@Service(value = "org.infodavid.commons.persistence.impl.ApplicationContextProvider")
public class ApplicationContextProvider implements ApplicationContextAware {

    /** The application context. */
    private static ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework .context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext; //NOSONAR Use the static field
    }

    /**
     * Gets the application context.
     * @return the application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
