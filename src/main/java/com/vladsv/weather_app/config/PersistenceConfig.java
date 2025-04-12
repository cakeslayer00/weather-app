package com.vladsv.weather_app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class PersistenceConfig {

    private final Environment environment;

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();

        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setPackagesToScan("com.vladsv.weather_app.entity");
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName(environment.getProperty("spring.datasource.driver"));
        config.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        config.setUsername(environment.getProperty("spring.datasource.user"));
        config.setPassword(environment.getProperty("spring.datasource.password"));
        setAdditionalConfigurationProperties(config);

        return new HikariDataSource(config);
    }

    private void setAdditionalConfigurationProperties(HikariConfig config) {
        config.setIdleTimeout(Integer.parseInt(Objects.requireNonNull(environment.getProperty("idleTimeout"))));
        config.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(environment.getProperty("maximumPoolSize"))));
        config.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(environment.getProperty("minimumIdle"))));
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
        hibernateProperties.setProperty("hibernate.highlight_sql", environment.getProperty("hibernate.highlight_sql"));

        return hibernateProperties;
    }

}
