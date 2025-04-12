package config;

import com.vladsv.weather_app.config.PersistenceConfig;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@PropertySource("classpath:application-test.properties")
@ComponentScan("com.vladsv.weather_app")
@Import({PersistenceConfig.class})
public class TestConfig implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext ac;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure().dataSource(ac.getBean(PersistenceConfig.class).dataSource()).load();
        flyway.migrate();
        return flyway;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ac = ac;
    }
}
