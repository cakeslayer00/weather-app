package com.vladsv.weather_app.config;

import com.vladsv.weather_app.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Profile("dev")
@Configuration
@ComponentScan(basePackages = {"com.vladsv.weather_app"})
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
@Import({PersistenceConfig.class,
        TemplateConfig.class,
        WebClientConfig.class,
        MapperConfig.class})
public class WebApplicationConfig extends WebMvcConfigurationSupport {

    private final AuthInterceptor authInterceptor;

    @Bean
    public Flyway flyway(PersistenceConfig config) {
        Flyway flyway = Flyway.configure().dataSource(config.dataSource()).load();
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
