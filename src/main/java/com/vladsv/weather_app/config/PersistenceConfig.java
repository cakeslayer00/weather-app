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

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class PersistenceConfig {

    //private final Environment environment;

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
        //sessionFactory.setHibernateProperties(hibernateProperties());

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

//    private Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//
//        hibernateProperties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
//        hibernateProperties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
//        hibernateProperties.setProperty("hibernate.highlight_sql", environment.getProperty("hibernate.highlight_sql"));
//
//        return hibernateProperties;
//    }

}
