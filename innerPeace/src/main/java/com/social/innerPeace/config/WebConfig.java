package com.social.innerPeace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/user/account/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/user/account/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}
