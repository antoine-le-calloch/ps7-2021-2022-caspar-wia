package me.polyfrontier.casparwia2.configuration;

import me.polyfrontier.casparwia2.authentification.UserTokenInterceptor;
import me.polyfrontier.casparwia2.model.UserEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@org.springframework.context.annotation.Configuration
public class PolyFrontierConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTokenInterceptor());
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor(currentUser());
    }

    @Bean
    public UserEntity currentUser() {
        return new UserEntity();
    }

}
