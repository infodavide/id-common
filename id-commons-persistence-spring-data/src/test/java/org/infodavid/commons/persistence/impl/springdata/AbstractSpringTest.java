package org.infodavid.commons.persistence.impl.springdata;

import org.infodavid.commons.test.TestCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class AbstractSpringTest.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringTestConfiguration.class
})
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "persistence.dialect=org.hibernate.dialect.HSQLDialect"
})
public abstract class AbstractSpringTest extends TestCase implements ApplicationContextAware {

    /** The application context. */
    protected ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework .context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
