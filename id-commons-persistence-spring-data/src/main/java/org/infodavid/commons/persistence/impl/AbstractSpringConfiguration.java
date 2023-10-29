package org.infodavid.commons.persistence.impl;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.infodavid.commons.persistence.impl.repository.QueryCallbackRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * The Class AbstractSpringConfiguration.
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.infodavid.commons.persistence.impl.repository", repositoryBaseClass = QueryCallbackRepositoryImpl.class)
@SuppressWarnings("static-method")
public abstract class AbstractSpringConfiguration {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSpringConfiguration.class);

    /**
     * Property sources placeholder configurer.
     * @return the property sources placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        final PropertySourcesPlaceholderConfigurer result = new PropertySourcesPlaceholderConfigurer();
        result.setIgnoreResourceNotFound(true);

        return result;
    }

    /** The auto commit. */
    @Value("${persistence.pool.autoCommit:true}")
    private boolean autoCommit;

    /** The connection timeout. */
    @Value("${persistence.pool.connectionTimeout:2000}")
    private int connectionTimeout;

    /** The database. */
    @Value("${persistence.database:db}")
    private String database;

    /** The dialect. */
    @Value("${persistence.dialect:org.hibernate.dialect.MariaDBDialect}")
    private String dialect;

    /** The hostname. */
    @Value("${persistence.hostname:localhost}")
    private String hostname;

    /** The idle timeout. */
    @Value("${persistence.pool.idleTimeout:1000}")
    private int idleTimeout;

    /** The initialization fail timeout. */
    @Value("${persistence.pool.initializationFailTimeout:2000}")
    private int initializationFailTimeout;

    /** The password. */
    @Value("${persistence.password:secret}")
    private String password;

    /** The pool maximum size. */
    @Value("${persistence.pool.maximumSize:50}")
    private int poolMaximumSize;

    /** The pool minimum size. */
    @Value("${persistence.pool.minimumSize:1}")
    private int poolMinimumSize;

    /** The port. */
    @Value("${persistence.port:3306}")
    private int port;

    /** The user name. */
    @Value("${persistence.username:db}")
    private String username;

    /** The validation timeout. */
    @Value("${persistence.pool.validationTimeout:2000}")
    private int validationTimeout;

    /**
     * Additional properties.
     * @return the properties
     */
    protected Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-only");
        properties.setProperty("hibernate.dialect", dialect);

        return properties;
    }

    /**
     * Data source.
     * @return the data source
     * @throws SQLException the SQL exception
     */
    @Bean
    @Profile("production")
    public DataSource dataSource() throws SQLException {
        LOGGER.debug("Creating the connection pool for database: {}", database);
        final HikariConfig config = new HikariConfig();
        config.setPoolName(database + "-pool");
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setJdbcUrl("jdbc:mariadb://" + hostname + ':' + port + '/' + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTestQuery("SELECT 1");
        config.setIdleTimeout(idleTimeout);
        config.setRegisterMbeans(false);
        config.setAutoCommit(autoCommit);
        config.setConnectionTimeout(connectionTimeout);
        config.setInitializationFailTimeout(initializationFailTimeout);
        config.setValidationTimeout(validationTimeout);
        config.setTransactionIsolation("TRANSACTION_REPEATABLE_READ");
        config.setMinimumIdle(poolMinimumSize);
        config.setMaximumPoolSize(poolMaximumSize);

        return new HikariDataSource(config); // NOSONAR Must be opened for later use
    }

    /**
     * Entity manager factory.
     * @return the local container entity manager factory bean
     * @throws SQLException the SQL exception
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource());
        result.setPackagesToScan("org.infodavid");
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        result.setJpaProperties(additionalProperties());

        return result;
    }

    /**
     * Exception translation.
     * @return the persistence exception translation post processor
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Transaction manager.
     * @return the platform transaction manager
     * @throws SQLException the SQL exception
     */
    @SuppressWarnings("resource")
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        final JpaTransactionManager result = new JpaTransactionManager();
        result.setEntityManagerFactory(entityManagerFactory().getObject());
        result.setNestedTransactionAllowed(true);

        return result;
    }
}
