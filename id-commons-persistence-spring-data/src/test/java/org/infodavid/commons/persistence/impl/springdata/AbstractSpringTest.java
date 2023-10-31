package org.infodavid.commons.persistence.impl.springdata;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.infodavid.commons.test.TestCase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class AbstractSpringTest.
 */
@SpringJUnitConfig(classes = AbstractSpringTest.SpringDateTestConfiguration.class)
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
        "persistence.dialect=org.hibernate.dialect.HSQLDialect"
})
@SuppressWarnings("static-method")
public abstract class AbstractSpringTest extends TestCase  {

    /**
     * The Class SpringDateTestConfiguration.
     */
    @Configuration
    @Import(SpringDataConfiguration.class)
    public static class SpringDateTestConfiguration {

        /**
         * Instantiates a new configuration.
         */
        public SpringDateTestConfiguration() {
            // noop
        }

        /**
         * Data source.
         * @return the data source
         * @throws SQLException the SQL exception
         */
        @Bean
        @Profile("test")
        public DataSource dataSource() throws SQLException {
            final DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
            dataSource.setUrl("jdbc:hsqldb:mem:testdb;DB_CLOSE_DELAY=-1");
            dataSource.setUsername("sa");
            dataSource.setPassword("");

            return dataSource;
        }
    }

    /** The application context. */
    protected ApplicationContext applicationContext;

    /**
     * Instantiates a new abstract spring test.
     */
    protected AbstractSpringTest() {
        // noop
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework .context.ApplicationContext)
     */
    //@Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
