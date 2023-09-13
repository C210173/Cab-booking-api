package com.sky.booking.response;

import com.sky.booking.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String jwt;
    private String message;
    private boolean isAuthenticated;
    private boolean isError;
    private String errorDetails;
    private UserRole type;
}