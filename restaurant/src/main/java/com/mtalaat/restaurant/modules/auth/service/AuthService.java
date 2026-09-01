package com.mtalaat.restaurant.modules.auth.service;

import com.mtalaat.restaurant.modules.auth.dto.AuthRequestDto;
import com.mtalaat.restaurant.modules.auth.dto.AuthResponseDto;
import com.mtalaat.restaurant.modules.auth.dto.SideMenuItemDto;
import com.mtalaat.restaurant.modules.auth.entity.RolePermission;
import com.mtalaat.restaurant.modules.auth.entity.User;
import com.mtalaat.restaurant.exceptions.UnauthorizedException;
import com.mtalaat.restaurant.modules.auth.mapping.UserMapper;
import com.mtalaat.restaurant.modules.auth.repository.UserRepository;
import com.mtalaat.restaurant.modules.auth.security.JwtService;
import com.mtalaat.restaurant.modules.auth.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RolePermissionService rolePermissionService;

    public AuthResponseDto login(AuthRequestDto request, String ipAddress) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtService.generateToken(principal);

            User user = userRepository.findById(principal.getId())
                    .orElseThrow(() -> new UnauthorizedException("msg_authenticated_user_could_not_be_loaded"));
            user.setLastLogin(LocalDateTime.now());
            user.setIpAddress(ipAddress);
            userRepository.save(user);


            List<Long> roleIds = user.getUserRoles().stream()
                    .map(userRole -> userRole.getRole().getId())
                    .toList();
            List<RolePermission> rolePermissionList = rolePermissionService.getRolePermissionsForRoles(roleIds);
            List<SideMenuItemDto> sideMenu = rolePermissionService.buildSideMenuForRolePermissions(rolePermissionList);


            return AuthResponseDto.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .user(userMapper.toDto(user))
                    .sideMenu(sideMenu)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("msg_invalid_email_or_password");
        }
    }

    public AuthResponseDto logout(String ipAddress) {
        //logout user
        return AuthResponseDto.builder()
                .accessToken(null)
                .tokenType(null)
                .user(null)
                .sideMenu(null)
                .build();
    }
}
