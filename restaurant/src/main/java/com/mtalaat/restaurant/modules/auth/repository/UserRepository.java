package com.mtalaat.restaurant.modules.auth.repository;

import com.mtalaat.restaurant.modules.auth.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @EntityGraph(attributePaths = {
            "userRoles",
            "userRoles.role",
            "userRoles.role.rolePermissions",
            "userRoles.role.rolePermissions.menuItem"
    })

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
