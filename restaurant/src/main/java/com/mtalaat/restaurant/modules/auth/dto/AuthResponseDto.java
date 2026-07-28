package com.mtalaat.restaurant.modules.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String tokenType;
    private UserDto user;
    private List<SideMenuItemDto> sideMenu;
}
