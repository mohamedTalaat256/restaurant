package com.mtalaat.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class FileAccessConfig implements WebMvcConfigurer {

    @Value("${app.files.base-dir:uploads}")
    private String baseStorageDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = Paths.get(baseStorageDir).toAbsolutePath().normalize().toUri().toString();
        if (!resourceLocation.endsWith("/")) {
            resourceLocation = resourceLocation + "/";
        }

        registry.addResourceHandler("/files/**")
                .addResourceLocations(resourceLocation);
    }
}