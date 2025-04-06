package com.vladsv.weather_app.config;

import com.vladsv.weather_app.dao.SessionDao;
import com.vladsv.weather_app.interceptor.AuthInterceptor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile({"dev", "test"})
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.vladsv.weather_app"})
@Import({PersistenceConfig.class,
        TemplateConfig.class,
        RestClientConfig.class,
        MapperConfig.class})
public class WebApplicationConfig implements ApplicationContextAware, WebMvcConfigurer {

    private ApplicationContext ac;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure().dataSource(ac.getBean(PersistenceConfig.class).dataSource()).load();
        flyway.migrate();
        return flyway;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(ac.getBean(SessionDao.class)))
                .excludePathPatterns("/auth/**", "/static/**");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**").addResourceLocations("/static/images/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.ac = ac;
    }
}
