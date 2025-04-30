package config;

import com.vladsv.weather_app.config.PersistenceConfig;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application-test.properties")
@ComponentScan("com.vladsv.weather_app")
@Import({PersistenceConfig.class})
public class TestConfig extends WebMvcConfigurationSupport {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return flyway;
    }

}
