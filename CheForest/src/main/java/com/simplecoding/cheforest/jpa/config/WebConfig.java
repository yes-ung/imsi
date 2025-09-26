package com.simplecoding.cheforest.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /upload/** 요청을 C:/upload/ 실제 폴더와 매핑
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///C:/upload/");
    }
}
