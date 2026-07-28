package com.mtalaat.restaurant.modules.auth.security;

import com.mtalaat.restaurant.modules.auth.entity.RolePermission;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.modules.auth.entity.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        for (UserRole userRole : user.getUserRoles()) {
            String roleName = userRole.getRole().getName();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

            for (RolePermission permission : userRole.getRole().getRolePermissions()) {
                Long menuId = permission.getMenuItem() != null ? permission.getMenuItem().getId() : null;
                if (menuId != null) {
                    if (Boolean.TRUE.equals(permission.getCanRead())) {
                        authorities.add(new SimpleGrantedAuthority("MENU_" + menuId + "_READ"));
                    }
                    if (Boolean.TRUE.equals(permission.getCanCreate())) {
                        authorities.add(new SimpleGrantedAuthority("MENU_" + menuId + "_CREATE"));
                    }
                    if (Boolean.TRUE.equals(permission.getCanEdit())) {
                        authorities.add(new SimpleGrantedAuthority("MENU_" + menuId + "_EDIT"));
                    }
                    if (Boolean.TRUE.equals(permission.getCanDelete())) {
                        authorities.add(new SimpleGrantedAuthority("MENU_" + menuId + "_DELETE"));
                    }
                }
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !Boolean.FALSE.equals(user.getStatus());
    }
}
