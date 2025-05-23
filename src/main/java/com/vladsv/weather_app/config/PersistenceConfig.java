package com.vladsv.weather_app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class PersistenceConfig {

    @Value("${db.url}")
    private String databaseUrl;

    @Value("${db.driver}")
    private String databaseDriver;

    @Value("${db.username}")
    private String databaseUsername;

    @Value("${db.password}")
    private String databasePassword;

    @Value("${db.maximumPoolSize}")
    private int databasePoolSize;

    @Value("${db.minimumIdle}")
    private int databaseMinimumIdle;

    @Value("${db.idleTimeout}")
    private long databaseIdleTimeout;

    @Value("hibernate.show_sql")
    private String hibernateShowSql;

    @Value("hibernate.format_sql")
    private String hibernateFormatSql;

    @Value("hibernate.highlight_sql")
    private String hibernateHighlightSql;

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
        config.setDriverClassName(databaseDriver);
        config.setJdbcUrl(databaseUrl);
        config.setUsername(databaseUsername);
        config.setPassword(databasePassword);
        config.setIdleTimeout(databaseIdleTimeout);
        config.setMaximumPoolSize(databasePoolSize);
        config.setMinimumIdle(databaseMinimumIdle);
        return new HikariDataSource(config);
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.show_sql", hibernateShowSql);
        hibernateProperties.setProperty("hibernate.format_sql", hibernateFormatSql);
        hibernateProperties.setProperty("hibernate.highlight_sql", hibernateHighlightSql);
        return hibernateProperties;
    }

}
