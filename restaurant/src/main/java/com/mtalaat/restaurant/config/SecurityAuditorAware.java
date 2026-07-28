package com.mtalaat.restaurant.config;

import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import com.mtalaat.restaurant.modules.auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<User> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userRepository.findById(userPrincipal.getId());
        }

        if (principal instanceof String username) {
            return userRepository.findByEmail(username);
        }

        return Optional.empty();
    }
}
