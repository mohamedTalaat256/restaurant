package com.mtalaat.restaurant.modules.auth.security;

import com.mtalaat.restaurant.exceptions.ForbiddenException;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {

    @Autowired
    UserRepository userRepository;

    public void checkPermission(Long menuId, String action) {

       /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ForbiddenException("msg_access_denied");
        }

        // Admin has full access
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return;
            }
        }

        String requiredAuthority = "MENU_" + menuId + "_" + action.toUpperCase();
        boolean hasPermission = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(requiredAuthority));

        if (!hasPermission) {
            throw new ForbiddenException("msg_access_denied");
        }*/
    }

    public void checkRead(Long menuId) {
        checkPermission(menuId, "READ");
    }

    public void checkCreate(Long menuId) {
        checkPermission(menuId, "CREATE");
    }

    public void checkEdit(Long menuId) {
        checkPermission(menuId, "EDIT");
    }

    public void checkDelete(Long menuId) {
        checkPermission(menuId, "DELETE");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        return userRepository.findById(userId).orElseThrow(() -> new ForbiddenException("msg_access_denied"));

    }
}
