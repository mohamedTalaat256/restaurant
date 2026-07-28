package com.mtalaat.restaurant.config;

import com.mtalaat.restaurant.modules.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
@RequiredArgsConstructor
public class AuditorAwareConfig {

    private final SecurityAuditorAware securityAuditorAware;

    @Bean
    public AuditorAware<User> auditorProvider() {
        return securityAuditorAware;
    }
}
