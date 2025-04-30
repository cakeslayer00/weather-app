package com.vladsv.weather_app.config;

import com.vladsv.weather_app.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.sql.DataSource;

@Profile("dev")
@Configuration
@ComponentScan(basePackages = {"com.vladsv.weather_app"})
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@EnableScheduling
@Import({PersistenceConfig.class,
        TemplateConfig.class,
        WebClientConfig.class,
        MapperConfig.class})
public class WebApplicationConfig extends WebMvcConfigurationSupport {

    private final AuthInterceptor authInterceptor;

    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return flyway;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/auth/**", "/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**").addResourceLocations("/static/images/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
    }

}
