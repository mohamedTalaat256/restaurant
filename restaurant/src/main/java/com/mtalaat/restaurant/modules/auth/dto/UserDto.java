package com.mtalaat.restaurant.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    private String about;
    private String waiterKitchenToken;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String image;
    private LocalDateTime lastLogin;
    private LocalDateTime lastLogout;
    private String ipAddress;
    private Integer counter;
    private Boolean status;
    private Boolean isAdmin;
    private Set<Long> roleIds;
}
